package live.yurii.ebot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class CampaignQueueService {

  private static final long MIN_DELAY_MS = 500;
  private static final long MAX_DELAY_MS = 1500;

  private final CampaignService campaignService;
  private final BlockingQueue<Integer> campaignQueue = new LinkedBlockingQueue<>();
  private volatile boolean processing = false;

  /**
   * Adds a campaign to the processing queue
   */
  public void enqueueCampaign(Integer campaignId) {
    try {
      boolean added = campaignQueue.offer(campaignId);
      if (added) {
        log.info("Campaign {} added to processing queue. Queue size: {}", campaignId, campaignQueue.size());
        // Start processing if not already running
        if (!processing) {
          processQueue();
        }
      } else {
        log.warn("Failed to add campaign {} to queue - queue might be full", campaignId);
      }
    } catch (Exception e) {
      log.error("Error adding campaign {} to queue", campaignId, e);
    }
  }

  /**
   * Processes the queue in a separate thread
   */
  @Async("campaignProcessorExecutor")
  public void processQueue() {
    if (processing) {
      log.debug("Queue processing already in progress");
      return;
    }

    processing = true;
    log.info("Starting campaign queue processing");

    try {
      while (!campaignQueue.isEmpty()) {
        Integer campaignId = campaignQueue.take(); // Blocks until item available
        log.info("Processing campaign {} from queue. Remaining: {}", campaignId, campaignQueue.size());

        try {
          campaignService.collectCampaignData(campaignId);
          log.info("Successfully processed campaign {}", campaignId);
        } catch (Exception e) {
          log.error("Error processing campaign {}", campaignId, e);
          // TODO: Add retry logic or dead letter queue
        }

        // Add delay between campaigns to be extra safe with rate limiting
        if (!campaignQueue.isEmpty()) {
          addRequestDelay();
        }

        // Add delay between pages within a campaign (handled by CampaignService)
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Queue processing interrupted", e);
    } finally {
      processing = false;
      log.info("Campaign queue processing completed");
    }
  }

  /**
   * Gets current queue size
   */
  public int getQueueSize() {
    return campaignQueue.size();
  }

  /**
   * Checks if queue is currently being processed
   */
  public boolean isProcessing() {
    return processing;
  }

  /**
   * Clears the queue (useful for testing or emergency reset)
   */
  public void clearQueue() {
    campaignQueue.clear();
    log.warn("Campaign queue cleared");
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