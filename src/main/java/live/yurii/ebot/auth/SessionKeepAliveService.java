package live.yurii.ebot.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionKeepAliveService {

  private final RestClient restClient;
  private final SessionContext sessionContext;
  private final AuthService authService;

  @Scheduled(
    initialDelayString = "#{@erepublikProperties.getKeepAlive().getInitialDelayMs()}",
    fixedRateString = "#{@erepublikProperties.getKeepAlive().getIntervalMinutes() * 60 * 1000}"
  )
  public void keepSessionAlive() {
    if (!sessionContext.isAuthenticated()) {
      log.warn("Not authenticated. Re-authenticating...");
      authService.authenticate();
      return;
    }

    try {
      log.debug("Sending keep-alive request...");

      MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
      requestData.add("countryId", "1");
      requestData.add("industryId", "1");
      requestData.add("quality", "1");
      requestData.add("orderBy", "price_asc");
      requestData.add("currentPage", "1");
      requestData.add("ajaxMarket", "1");
      requestData.add("_token", sessionContext.getCsrfToken());

      String response = restClient.post()
        .uri("/en/economy/marketplaceAjax")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header("X-Requested-With", "XMLHttpRequest")
        .header("Accept", "application/json, text/plain, */*")
        .header("Sec-Fetch-Dest", "empty")
        .header("Sec-Fetch-Mode", "cors")
        .body(requestData)
        .retrieve()
        .onStatus(HttpStatus.UNAUTHORIZED::equals, (req, resp) -> {
          log.warn("Session expired (401)");
          sessionContext.logout();
        })
        .body(String.class);

      log.debug("Keep-alive successful: {}", response);

    } catch (Exception e) {
      log.error("Keep-alive failed: {}", e.getMessage());
      if (e.getMessage().contains("401") || e.getMessage().contains("403")) {
        sessionContext.logout();
      }
    }
  }

}
