package live.yurii.ebot.dto;

import live.yurii.ebot.model.Country;
import live.yurii.ebot.model.WarType;

import java.time.Instant;

public record BattleDTO(
  int id,
  int warId,
  Instant start,
  WarType warType,
  Country invader,
  Country defender
) {
}
