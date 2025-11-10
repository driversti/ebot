package live.yurii.ebot.service;

import live.yurii.ebot.dto.BattleDTO;
import live.yurii.ebot.dto.CampaignsListDTO;
import live.yurii.ebot.entity.Campaign;
import live.yurii.ebot.entity.City;
import live.yurii.ebot.entity.Region;
import live.yurii.ebot.repository.CampaignRepository;
import live.yurii.ebot.repository.CityRepository;
import live.yurii.ebot.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignSyncService {

  private final RestClient restClient;
  private final CampaignRepository campaignRepository;
  private final CampaignQueueService campaignQueueService;
  private final RegionRepository regionRepository;
  private final CityRepository cityRepository;

  @Scheduled(fixedDelay = 300000) // 5 minutes in milliseconds
  @Transactional
  public void syncCampaigns() {
    log.info("Starting optimized campaign sync...");

    try {
      // Step 1: Fetch all ongoing campaigns from API
      CampaignsListDTO campaignsList = fetchCampaignsFromApi();
      List<Integer> ongoingCampaignIds = campaignsList.battles().stream()
        .map(BattleDTO::id)
        .toList();

      log.info("Fetched {} ongoing campaigns from API", ongoingCampaignIds.size());

      // Step 2: Get only the IDs of already saved campaigns from DB (optimized query)
      Set<Integer> savedCampaignIds = new HashSet<>(campaignRepository.findExistingIds(ongoingCampaignIds));

      log.info("Found {} campaigns already saved in DB", savedCampaignIds.size());

      // Step 3: Filter to get only new campaigns
      List<BattleDTO> newBattles = campaignsList.battles().stream()
        .filter(battle -> !savedCampaignIds.contains(battle.id()))
        .toList();

      log.info("Found {} new campaigns to create", newBattles.size());

      // Step 4: Create domain models only for new campaigns
      List<Campaign> newCampaigns = newBattles.stream()
        .map(this::createCampaignDomainModel)
        .toList();
      campaignRepository.saveAll(newCampaigns);

      log.info("Campaign sync completed. Created {} new campaigns, Skipped {} existing campaigns",
        newCampaigns.size(), savedCampaignIds.size());
    } catch (Exception e) {
      log.error("Error during campaign sync", e);
    }
  }

  private CampaignsListDTO fetchCampaignsFromApi() {
    return restClient.get()
      .uri("/en/military/campaignsJson/list")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .body(CampaignsListDTO.class);
  }

  private Campaign createCampaignDomainModel(BattleDTO battle) {
    log.debug("Creating domain model for new campaign: {}", battle.id());

    // Create Region using actual game data
    Region region = regionRepository.findById(battle.region().id())
      .orElseGet(() -> regionRepository.save(
        Region.builder().id(battle.region().id()).name(battle.region().name()).build()
      ));

    // Create City using actual game data
    City city = cityRepository.findById(battle.city().id())
      .orElseGet(() -> cityRepository.save(
        City.builder().id(battle.city().id()).name(battle.city().name()).region(region).build()
      ));

    // Create the main Campaign entity with all relationships
    return Campaign.builder()
      .id(battle.id())
      .warId(battle.warId())
      .startedAt(null) // find the correct start time
      .finishedAt(null) // Ongoing campaign
      .invader(battle.invader())
      .defender(battle.defender())
      .region(region)
      .city(city)
      .warType(battle.warType())
      .build();
  }
}
