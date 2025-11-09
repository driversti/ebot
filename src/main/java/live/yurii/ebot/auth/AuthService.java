package live.yurii.ebot.auth;

import live.yurii.ebot.config.ErepublikProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final RestClient restClient;
  private final SessionContext sessionContext;
  private final ErepublikProperties properties;

  private final AtomicBoolean authInProgress = new AtomicBoolean(false);

  @Retryable(retryFor = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
  public void authenticate() {
    // Avoid unnecessary re-login if we are already authenticated
    if (sessionContext.isAuthenticated()) {
      log.debug("Already authenticated — skipping authenticate()");
      return;
    }
    if (!authInProgress.compareAndSet(false, true)) {
      log.info("Authentication already in progress, skipping");
      return;
    }
    try {
      log.info("Starting authentication for: {}", properties.getLogin().getEmail());
      sessionContext.logout();

      // Step 1: Get the login page and extract CSRF token
      String loginPage = restClient.get()
        .uri("/en")
        .retrieve()
        .body(String.class);
      String initialToken = extractCsrfToken(loginPage);
      log.debug("Initial token: {}", initialToken);

      // Step 2: Submit a login form
      MultiValueMap<String, String> loginForm = new LinkedMultiValueMap<>();
      loginForm.add("_token", initialToken);
      loginForm.add("citizen_email", properties.getLogin().getEmail());
      loginForm.add("citizen_password", properties.getLogin().getPassword());
      loginForm.add("remember", "on");

      ResponseEntity<String> response = restClient.post()
        .uri("/en/login")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(loginForm)
        .retrieve()
        .toEntity(String.class);

      log.debug("Login response: {}", response.getStatusCode());

      // Step 3: Extract permanent token from dashboard
      String dashboard = restClient.get()
        .uri("/en")
        .retrieve()
        .body(String.class);

      String permanentToken = extractCsrfToken(dashboard);
      sessionContext.setCsrfToken(permanentToken);

      log.info("Authentication successful. CSRF token: {}", permanentToken);

    } catch (Exception e) {
      log.error("Authentication failed: {}", e.getMessage());
      sessionContext.logout();
      throw new RuntimeException("Authentication failed", e);
    } finally {
      authInProgress.set(false);
    }
  }

  private String extractCsrfToken(String html) {
    Document doc = Jsoup.parse(html);
    Element tokenElement = doc.selectFirst("input[name=_token]");
    if (tokenElement == null) {
      throw new IllegalStateException("CSRF token not found in HTML");
    }
    return tokenElement.val();
  }
}
