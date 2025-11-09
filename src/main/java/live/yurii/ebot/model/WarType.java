package live.yurii.ebot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarType {

  DIRECT("direct"),
  RESISTANCE("resistance"),
  AIRSTRIKE("airstrike"),
  LIBERATION("liberation");

  private final String name;

  public static WarType getByName(String name) {
    for (WarType warType : WarType.values()) {
      if (warType.name.equalsIgnoreCase(name)) {
        return warType;
      }
    }
    throw new IllegalArgumentException(String.format("No enum constant %s", name));
  }
}
