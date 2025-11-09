package live.yurii.ebot.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import live.yurii.ebot.dto.BattleConsoleDTO;
import live.yurii.ebot.utils.DeserializerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleConsoleDTODeserializer extends AbstractDeserializer<BattleConsoleDTO> {

  @Override
  protected BattleConsoleDTO deserialize(JsonNode node) {
    List<BattleConsoleDTO.CombatContributionDTO> contributions = new ArrayList<>();
    AtomicInteger maxPages = new AtomicInteger(0);
    List<BattleConsoleDTO.RoundInfoDTO> rounds = new ArrayList<>();

    // Process country sides (fighter data)
    DeserializerUtil.toStream(node.fieldNames())
      .filter(fieldName -> !fieldName.equals("rounds"))
      .filter(fieldName -> fieldName.matches("\\d+")) // Only numeric country IDs
      .forEach(countryId -> {
        JsonNode countryNode = node.get(countryId);
        JsonNode fighterData = countryNode.get("fighterData");

        if (fighterData != null && fighterData.isObject()) {
          // Extract contributions
          DeserializerUtil.toStream(fighterData.elements())
            .forEach(jn -> {
              BattleConsoleDTO.CombatContributionDTO contribution =
                BattleConsoleDTO.CombatContributionDTO.builder()
                  .citizenId(jn.get("citizenId").asInt())
                  .citizenName(jn.get("citizenName").asText())
                  .forCountryId(jn.get("for_country_id").asInt())
                  .rawValue(jn.get("raw_value").asLong())
                  .build();
              contributions.add(contribution);
            });
        }

        // Track max pages
        JsonNode pagesNode = countryNode.get("pages");
        if (pagesNode != null) {
          int pages = pagesNode.asInt();
          maxPages.set(Math.max(maxPages.get(), pages));
        }
      });

    // Process rounds information
    JsonNode roundsNode = node.get("rounds");
    if (roundsNode != null && roundsNode.isObject()) {
      DeserializerUtil.toStream(roundsNode.elements())
        .forEach(jn -> {
          BattleConsoleDTO.RoundInfoDTO roundInfo = BattleConsoleDTO.RoundInfoDTO.builder()
            .id(jn.get("id").asInt())
            .round(jn.get("round").asInt())
            .division(jn.get("division").asInt())
            .createdAt(jn.get("created_at").asLong())
            .build();
          rounds.add(roundInfo);
        });
    }

    return BattleConsoleDTO.builder()
      .contributions(contributions)
      .maxPages(maxPages.get())
      .rounds(rounds)
      .build();
  }
}
