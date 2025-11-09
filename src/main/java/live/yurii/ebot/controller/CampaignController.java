package live.yurii.ebot.controller;

import live.yurii.ebot.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

  private final CampaignService campaignService;

  @PostMapping("/{campaignId}/collect")
  public ResponseEntity<Void> collectCampaignData(@PathVariable Integer campaignId) {
    log.info("Collecting data for campaign ID: {}", campaignId);
    campaignService.collectCampaignData(campaignId);
    return ResponseEntity.accepted().build();
  }
}
