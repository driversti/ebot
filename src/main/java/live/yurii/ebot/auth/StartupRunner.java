package live.yurii.ebot.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

  private final AuthService authService;

  @Override
  public void run(String... args) {
    log.info("Performing initial authentication...");
    try {
      authService.authenticate();
    } catch (Exception e) {
      log.error("Initial authentication failed: {}", e.getMessage());
    }

  }
}
