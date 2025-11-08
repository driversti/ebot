package live.yurii.ebot.auth;

import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SessionContext {

  private volatile String csrfToken;
  private final BasicCookieStore cookieStore = new BasicCookieStore();

  public boolean isAuthenticated() {
    return csrfToken != null && !cookieStore.getCookies().isEmpty();
  }

  public void logout() {
    csrfToken = null;
    cookieStore.clear();
  }
}
