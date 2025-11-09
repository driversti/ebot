package live.yurii.ebot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Division {
  D1((short) 1),
  D2((short) 2),
  D3((short) 3),
  D4((short) 4),
  D11((short) 11);

  private final short number;

  public static Division fromNumber(short number) {
    for (Division division : values()) {
      if (division.number == number) {
        return division;
      }
    }
    throw new IllegalArgumentException("Invalid division number: " + number);
  }

  public short getNumber() {
    return number;
  }
}
