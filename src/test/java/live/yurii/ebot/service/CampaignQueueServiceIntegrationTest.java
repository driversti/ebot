package live.yurii.ebot.service;

import live.yurii.ebot.config.AsyncConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestMethod;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;

@Slf4j
@SpringBootTest
@Import(AsyncConfig.class)
@TestPropertySource(properties = {
    "spring.task.execution.pool.core-size=1",
    "spring.task.execution.pool.max-size=1",
    "spring.task.execution.pool.queue-capacity=10"
})
class CampaignQueueServiceIntegrationTest {

  @Autowired
  private CampaignQueueService campaignQueueService;

  @MockBean
  private CampaignService campaignService;

  @AfterTestMethod
  void cleanup() {
    campaignQueueService.clearQueue();
  }

  @Test
  void testAsyncProcessingWorks() throws InterruptedException {
    // Given
    CountDownLatch processingLatch = new CountDownLatch(3);
    AtomicInteger processedCount = new AtomicInteger(0);

    doAnswer(invocation -> {
      Integer campaignId = invocation.getArgument(0);
      log.info("Mock processing campaign: {}", campaignId);
      Thread.sleep(100); // Simulate processing time
      processedCount.incrementAndGet();
      processingLatch.countDown();
      return null;
    }).when(campaignService).collectCampaignData(anyInt());

    // When
    campaignQueueService.enqueueCampaign(1);
    campaignQueueService.enqueueCampaign(2);
    campaignQueueService.enqueueCampaign(3);

    // Then
    assertThat(processingLatch.await(5, TimeUnit.SECONDS))
        .as("All campaigns should be processed within timeout")
        .isTrue();

    assertThat(processedCount.get())
        .as("All campaigns should be processed")
        .isEqualTo(3);

    assertThat(campaignQueueService.getQueueSize())
        .as("Queue should be empty after processing")
        .isEqualTo(0);

    // Give a moment for the callback to execute
    Thread.sleep(200);

    assertThat(campaignQueueService.isProcessing())
        .as("Processing flag should be false after completion")
        .isFalse();
  }

  @Test
  void testSequentialProcessing() throws InterruptedException {
    // Given
    int numberOfCampaigns = 5;
    CountDownLatch processingLatch = new CountDownLatch(numberOfCampaigns);
    AtomicInteger processedCount = new AtomicInteger(0);

    doAnswer(invocation -> {
      Integer campaignId = invocation.getArgument(0);
      log.info("Mock processing campaign: {}", campaignId);
      Thread.sleep(100); // Simulate processing time
      processedCount.incrementAndGet();
      processingLatch.countDown();
      return null;
    }).when(campaignService).collectCampaignData(anyInt());

    // When - enqueue campaigns sequentially
    for (int i = 1; i <= numberOfCampaigns; i++) {
      campaignQueueService.enqueueCampaign(i);
    }

    // Then
    assertThat(processingLatch.await(10, TimeUnit.SECONDS))
        .as("All campaigns should be processed within timeout")
        .isTrue();

    assertThat(processedCount.get())
        .as("All campaigns should be processed")
        .isEqualTo(numberOfCampaigns);

    assertThat(campaignQueueService.getQueueSize())
        .as("Queue should be empty after processing")
        .isEqualTo(0);
  }

  @Test
  void testProcessingStatus() throws InterruptedException {
    // Given
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch completeLatch = new CountDownLatch(1);

    doAnswer(invocation -> {
      log.info("Mock processing campaign with delay");
      startLatch.countDown();
      Thread.sleep(500); // Simulate longer processing
      completeLatch.countDown();
      return null;
    }).when(campaignService).collectCampaignData(anyInt());

    // When
    campaignQueueService.enqueueCampaign(1);

    // Then
    assertThat(startLatch.await(2, TimeUnit.SECONDS))
        .as("Processing should start")
        .isTrue();

    assertThat(campaignQueueService.isProcessing())
        .as("Processing flag should be true during processing")
        .isTrue();

    assertThat(completeLatch.await(2, TimeUnit.SECONDS))
        .as("Processing should complete")
        .isTrue();

    // Give a moment for the callback to execute
    Thread.sleep(100);

    assertThat(campaignQueueService.isProcessing())
        .as("Processing flag should be false after completion")
        .isFalse();
  }
}