package live.yurii.ebot.service;

import live.yurii.ebot.auth.SessionContext;
import live.yurii.ebot.dto.BattleConsoleDTO;
import live.yurii.ebot.dto.BattleStatsDTO;
import live.yurii.ebot.entity.Campaign;
import live.yurii.ebot.entity.Citizen;
import live.yurii.ebot.entity.CombatContribution;
import live.yurii.ebot.entity.Division;
import live.yurii.ebot.entity.Round;
import live.yurii.ebot.model.Country;
import live.yurii.ebot.repository.CampaignRepository;
import live.yurii.ebot.repository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CampaignService {

  private static final int FIRST = 1;

  private final RestClient restClient;
  private final SessionContext sessionContext;
  private final CampaignRepository campaignRepository;
  private final CitizenRepository citizenRepository;

  @Transactional
  public void collectCampaignData(Integer campaignId) {
    log.info("Starting data collection for campaign ID: {}", campaignId);

    try {
      // Check if campaign exists
      Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
      if (campaign == null) {
        log.warn("Campaign {} not found in database. Cannot collect data.", campaignId);
        // TODO: Send notification to developer that campaign needs to be created first
        return;
      }

      // Step 1: Fetch battle stats to get round IDs
      BattleStatsDTO battleStats = fetchBattleStats(campaignId);
      log.info("Found {} rounds for campaign ID: {}", battleStats.getRoundIds().size(), campaignId);

      // Step 2: Fetch battle console data with any round to get a complete list of all rounds
      Integer firstRoundId = battleStats.getRoundIds().getFirst();
      log.info("Fetching battle console data with round {} to get complete round list", firstRoundId);

      BattleConsoleDTO consoleData = fetchBattleConsoleData(campaignId, FIRST, FIRST, FIRST, firstRoundId, FIRST, FIRST);
      log.info("Found total {} rounds in battle console", consoleData.getRounds().size());

      // Step 3: Iterate over all rounds and pull contributions with pagination
      int totalContributions = 0;
      for (BattleConsoleDTO.RoundInfoDTO roundInfo : consoleData.getRounds()
        .stream()
        .sorted(Comparator.comparing(BattleConsoleDTO.RoundInfoDTO::getId))
        .toList()
      ) {
        log.info("Processing round {} (division {}, order {})",
          roundInfo.getId(), roundInfo.getDivision(), roundInfo.getRound());

        // Find or create round through campaign domain method
        Optional<Round> roundOpt = campaign.findRoundById(roundInfo.getId());
        Round round = roundOpt.orElseGet(() -> createNewRound(campaign, roundInfo));

        // Pull contributions for all pages
        int roundContributions = 0;
        for (int page = 1; page <= consoleData.getMaxPages(); page++) {
          log.debug("Fetching page {} of {} for round {}", page, consoleData.getMaxPages(), roundInfo.getId());

          BattleConsoleDTO pageData = fetchBattleConsoleData(campaignId, roundInfo.getRound(),
            roundInfo.getRound(), roundInfo.getDivision(), roundInfo.getId(), page, page
          );

          log.debug("Page {}: Found {} contributions", page, pageData.getContributions().size());

          // Store contributions
          storeContributions(round, pageData.getContributions());
          roundContributions += pageData.getContributions().size();
        }

        totalContributions += roundContributions;
        log.info("Round {}: Processed {} contributions", roundInfo.getId(), roundContributions);
      }

      log.info("Data collection completed for campaign ID: {}. Total contributions: {}", campaignId, totalContributions);

    } catch (Exception e) {
      log.error("Error collecting data for campaign ID: {}", campaignId, e);
      throw new RuntimeException("Failed to collect campaign data", e);
    }
  }

  private BattleStatsDTO fetchBattleStats(Integer campaignId) {
    String url = "/en/military/battle-stats/" + campaignId;

    return restClient.get()
      .uri(url)
      .retrieve()
      .body(BattleStatsDTO.class);
  }

  private BattleConsoleDTO fetchBattleConsoleData(Integer battleId, Integer zoneId, Integer roundOrder, Integer division, Integer battleZoneId, int leftPage, int rightPage) {
    String csrfToken = sessionContext.getCsrfToken();

    String body = String.format(
      "battleId=%d&zoneId=%d&action=battleStatistics&round=%d&division=%d&battleZoneId=%d&type=damage&leftPage=%d&rightPage=%d&_token=%s",
      battleId, zoneId, roundOrder, division, battleZoneId, leftPage, rightPage, csrfToken
    );
    log.info(body);

    return restClient.post()
      .uri("/en/military/battle-console")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(body)
      .retrieve()
      .body(BattleConsoleDTO.class);
  }

  private Round createNewRound(Campaign campaign, BattleConsoleDTO.RoundInfoDTO roundInfo) {
    // Create new round
    Division division = Division.fromNumber(roundInfo.getDivision().shortValue());
    Round round = Round.builder()
      .id(roundInfo.getId())
      .round(roundInfo.getRound().shortValue())
      .division(division)
      .startedAt(Instant.ofEpochSecond(roundInfo.getCreatedAt()))
      .campaign(campaign)
      .invaderScore(0)
      .defenderScore(0)
      .contributions(new HashSet<>())
      .build();

    // Add round through campaign's domain method
    campaign.addRound(round);
    return round;
  }

  private void storeContributions(Round round, List<BattleConsoleDTO.CombatContributionDTO> contributions) {
    for (BattleConsoleDTO.CombatContributionDTO dto : contributions) {
      // Find or create citizen
      Citizen citizen = citizenRepository.findById(dto.getCitizenId())
        .orElseGet(() -> {
          Citizen newCitizen = Citizen.builder().id(dto.getCitizenId()).name(dto.getCitizenName()).build();
          return citizenRepository.save(newCitizen);
        });

      // Create combat contribution
      CombatContribution contribution = CombatContribution.builder()
        .citizen(citizen)
        .forCountry(Country.getById(dto.getForCountryId()))
        .damage(dto.getRawValue())
        .round(round)
        .build();

      round.addContribution(contribution);
    }
  }

}
