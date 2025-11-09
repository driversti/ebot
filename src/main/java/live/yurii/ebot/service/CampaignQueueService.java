package live.yurii.ebot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Service
public class CampaignQueueService {

  private static final int MAX_QUEUE_SIZE = 1000; // Prevent memory issues

  private final CampaignProcessorService campaignProcessorService;
  private final BlockingQueue<Integer> campaignQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
  private final AtomicBoolean processing = new AtomicBoolean(false);

  /**
   * Adds a campaign to the processing queue
   */
  public void enqueueCampaign(Integer campaignId) {
    try {
      boolean added = campaignQueue.offer(campaignId);
      if (added) {
        log.info("Campaign {} added to processing queue. Queue size: {}", campaignId, campaignQueue.size());
        // Start processing if not already running
        if (!processing.get()) {
          tryStartProcessing();
        }
      } else {
        log.warn("Failed to add campaign {} to queue - queue might be full", campaignId);
      }
    } catch (Exception e) {
      log.error("Error adding campaign {} to queue", campaignId, e);
    }
  }

  /**
   * Thread-safe method to start processing if not already running
   */
  private void tryStartProcessing() {
    if (processing.compareAndSet(false, true)) {
      log.debug("Starting queue processing thread");
      // Use the separate service to enable true async processing via Spring AOP
      campaignProcessorService.processCampaignsAsync(campaignQueue, () -> {
        // mark processing finished, then re-check for any items that arrived in the window
        processing.set(false);
        if (!campaignQueue.isEmpty()) {
          tryStartProcessing();
        }
      });
    } else {
      log.debug("Queue processing already in progress");
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
    return processing.get();
  }

  /**
   * Clears the queue (useful for testing or emergency reset)
   */
  public void clearQueue() {
    campaignQueue.clear();
    log.warn("Campaign queue cleared");
  }

}
