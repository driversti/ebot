package live.yurii.ebot.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import live.yurii.ebot.dto.BattleStatsDTO;
import live.yurii.ebot.utils.DeserializerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BattleStatsDTODeserializer extends AbstractDeserializer<BattleStatsDTO> {

  @Override
  protected BattleStatsDTO deserialize(JsonNode node) throws IOException {
    BattleStatsDTO dto = new BattleStatsDTO();

    JsonNode battleZoneSituation = node.get("battle_zone_situation");
    if (battleZoneSituation == null || !battleZoneSituation.isObject()) {
      return dto;
    }

    List<Integer> roundIds = DeserializerUtil.toStream(battleZoneSituation.fieldNames())
      .map(Integer::parseInt)
      .toList();

    dto.setRoundIds(roundIds);
    return dto;
  }
}
