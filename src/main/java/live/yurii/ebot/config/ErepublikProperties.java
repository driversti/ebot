package live.yurii.ebot.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "erepublik")
public class ErepublikProperties {

  private String baseUrl = "https://erepublik.com";
  private Login login = new Login();
  private KeepAlive keepAlive = new KeepAlive();
  private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Login {
    private String email;
    private String password;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class KeepAlive {
    private int intervalMinutes = 5;
    private long initialDelayMs = 15000;
  }
}
