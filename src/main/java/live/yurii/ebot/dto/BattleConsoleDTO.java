package live.yurii.ebot.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import live.yurii.ebot.deserializer.BattleConsoleDTODeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonDeserialize(using = BattleConsoleDTODeserializer.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleConsoleDTO {
  private List<CombatContributionDTO> contributions;
  private int maxPages;
  private List<RoundInfoDTO> rounds;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CombatContributionDTO {
    private Integer citizenId;
    private String citizenName;
    private Integer forCountryId;
    private Long rawValue;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoundInfoDTO {
    private Integer id;
    private Integer round;
    private Integer division;
    private Long createdAt;
  }
}
