package live.yurii.ebot.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import live.yurii.ebot.deserializer.BattleStatsDTODeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonDeserialize(using = BattleStatsDTODeserializer.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleStatsDTO {
  private List<Integer> roundIds;

  public List<Integer> getRoundIds() {
    return roundIds == null ? List.of() : List.copyOf(roundIds);
  }
}
