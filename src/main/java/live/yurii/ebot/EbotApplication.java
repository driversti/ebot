package live.yurii.ebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
public class EbotApplication {

  static void main(String[] args) {
    SpringApplication.run(EbotApplication.class, args);
  }

}
