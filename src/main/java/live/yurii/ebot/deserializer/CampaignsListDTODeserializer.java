package live.yurii.ebot.deserializer;

import static live.yurii.ebot.utils.DeserializerUtil.toInstant;
import static live.yurii.ebot.utils.DeserializerUtil.toStream;

import com.fasterxml.jackson.databind.JsonNode;
import live.yurii.ebot.dto.BattleDTO;
import live.yurii.ebot.dto.CampaignsListDTO;
import live.yurii.ebot.model.Country;
import live.yurii.ebot.model.WarType;

import java.time.Instant;
import java.util.List;

public class CampaignsListDTODeserializer extends AbstractDeserializer<CampaignsListDTO> {

  @Override
  public CampaignsListDTO deserialize(JsonNode node) {

    List<BattleDTO> battles = toStream(node.get("battles").elements())
      .map(CampaignsListDTODeserializer::toBattle).toList();

    List<Country> countries = toStream(node.get("countries").elements())
      .map(jn -> Country.getById(jn.get("id").asInt())).toList();

    Instant updatedAt = toInstant(node.get("last_updated"));
    Instant time = toInstant(node.get("time"));

    return new CampaignsListDTO(battles, countries, updatedAt, time);
  }

  private static BattleDTO toBattle(JsonNode jn) {
    return new BattleDTO(
      jn.get("id").asInt(),
      jn.get("war_id").asInt(),
      Instant.ofEpochSecond(jn.get("start").asLong()),
      WarType.getByName(jn.get("war_type").asText()),
      Country.getById(jn.get("inv").get("id").asInt()),
      Country.getById(jn.get("def").get("id").asInt())
    );
  }
}
