package live.yurii.ebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

  /**
   * Single-threaded executor for campaign processing to avoid rate limiting (429 errors)
   * Uses a queue to handle multiple campaigns sequentially
   */
  @Bean("campaignProcessorExecutor")
  public Executor campaignProcessorExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(1);
    executor.setQueueCapacity(0); // We use our own queue
    executor.setThreadNamePrefix("CampaignProcessor-");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);
    executor.initialize();
    return executor;
  }
}