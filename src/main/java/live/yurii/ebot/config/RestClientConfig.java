package live.yurii.ebot.config;

import live.yurii.ebot.auth.SessionContext;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

  private final SessionContext sessionContext;
  private final ErepublikProperties properties;

  @Bean
  public RestClient restClient() {
    PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
      .setMaxConnTotal(20)
      .setMaxConnPerRoute(10)
      .build();

    CloseableHttpClient httpClient = HttpClients.custom()
      .setConnectionManager(connectionManager)
      .setDefaultCookieStore(sessionContext.getCookieStore())
      .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

    return RestClient.builder()
      .requestFactory(requestFactory)
      .baseUrl(properties.getBaseUrl())
      .defaultHeader("User-Agent", properties.getUserAgent())
      .defaultHeader("Accept-Language", "en-GB,en;q=0.9")
      .defaultHeader("Sec-GPC", "1")
      .defaultHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*")
      .defaultHeader("Sec-Fetch-Dest", "document")
      .defaultHeader("Sec-Fetch-Mode", "navigate")
      .defaultHeader("Sec-Fetch-Site", "none")
      .defaultHeader("Referer", "https://erepublik.com/en")
      .build();
  }
}
