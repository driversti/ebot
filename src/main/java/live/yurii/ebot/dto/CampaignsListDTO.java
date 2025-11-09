package live.yurii.ebot.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import live.yurii.ebot.deserializer.CampaignsListDTODeserializer;
import live.yurii.ebot.model.Country;

import java.time.Instant;
import java.util.List;

@JsonDeserialize(using = CampaignsListDTODeserializer.class)
public record CampaignsListDTO(
  List<BattleDTO> battles,
  List<Country> countries,
  Instant lastUpdated,
  Instant time
) {
}
