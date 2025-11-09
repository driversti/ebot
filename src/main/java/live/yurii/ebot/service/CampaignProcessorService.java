package live.yurii.ebot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class CampaignProcessorService {

  private static final long MIN_DELAY_MS = 500;
  private static final long MAX_DELAY_MS = 1500;

  private final CampaignService campaignService;

  /**
   * Processes campaigns from the queue asynchronously
   * This method is called via Spring AOP proxy to enable true async processing
   */
  @Async("campaignProcessorExecutor")
  public void processCampaignsAsync(BlockingQueue<Integer> campaignQueue, Runnable completionCallback) {
    log.info("Starting campaign queue processing");
    int processedCount = 0;
    int errorCount = 0;

    try {
      while (true) {
        Integer campaignId = campaignQueue.poll(300, TimeUnit.MILLISECONDS);
        if (campaignId == null) { // idle window passed with no new items — consider queue drained
          break;
        }
        processedCount++;
        log.info("Processing campaign {} from queue. Remaining: {}", campaignId, campaignQueue.size());

        try {
          campaignService.collectCampaignData(campaignId);
          log.info("Successfully processed campaign {} ({} processed, {} errors)",
                   campaignId, processedCount, errorCount);
        } catch (Exception e) {
          errorCount++;
          log.error("Error processing campaign {} ({} processed, {} errors)",
                   campaignId, processedCount, errorCount, e);
          // TODO: Add retry logic or dead letter queue
        }

        // Add delay between campaigns to be extra safe with rate limiting
        if (!campaignQueue.isEmpty()) {
          addRequestDelay();
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Queue processing interrupted after processing {} campaigns with {} errors",
               processedCount, errorCount, e);
    } finally {
      // Notify completion callback
      if (completionCallback != null) {
        completionCallback.run();
      }
      log.info("Campaign queue processing completed. Processed: {}, Errors: {}", processedCount, errorCount);
    }
  }

  /**
   * Adds delay between requests to avoid 429 rate limiting
   */
  private void addRequestDelay() {
    try {
      long delay = ThreadLocalRandom.current().nextLong(MIN_DELAY_MS, MAX_DELAY_MS + 1);
      log.debug("Adding delay of {} ms between campaign processing", delay);
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Request delay interrupted", e);
    }
  }
}
