package live.yurii.ebot.controller;

import live.yurii.ebot.service.CampaignQueueService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

  private final CampaignQueueService campaignQueueService;

  @PostMapping("/{campaignId}/collect")
  public ResponseEntity<Void> collectCampaignData(@PathVariable Integer campaignId) {
    log.info("Collecting data for campaign ID: {}", campaignId);
    campaignQueueService.enqueueCampaign(campaignId);
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/queue/status")
  public ResponseEntity<QueueStatusDTO> getQueueStatus() {
    return ResponseEntity.ok(new QueueStatusDTO(
      campaignQueueService.getQueueSize(),
      campaignQueueService.isProcessing()
    ));
  }

  @DeleteMapping("/queue/clear")
  public ResponseEntity<Void> clearQueue() {
    campaignQueueService.clearQueue();
    return ResponseEntity.noContent().build();
  }

  @Data
  @AllArgsConstructor
  private static class QueueStatusDTO {
    private int queueSize;
    private boolean processing;
  }
}
