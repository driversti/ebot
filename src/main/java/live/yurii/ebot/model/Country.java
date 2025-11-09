package live.yurii.ebot.model;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Country {
  WORLD(0, "World", "cc", "\uD83C\uDF0D"),
  ALBANIA(167, "Albania", "ALL", "\uD83C\uDDE6\uD83C\uDDF1"),
  ARGENTINA(27, "Argentina", "ARS", "\uD83C\uDDE6\uD83C\uDDF7"),
  ARMENIA(169, "Armenia", "AMD", "\uD83C\uDDE6\uD83C\uDDF2"),
  AUSTRALIA(50, "Australia", "AUD", "\uD83C\uDDE6\uD83C\uDDFA"),
  AUSTRIA(33, "Austria", "ATS", "\uD83C\uDDE6\uD83C\uDDF9"),
  BELARUS(83, "Belarus", "BYN", "\uD83C\uDDE7\uD83C\uDDFE"),
  BELGIUM(32, "Belgium", "BEF", "\uD83C\uDDE7\uD83C\uDDEA"),
  BOLIVIA(76, "Bolivia", "BOB", "\uD83C\uDDE7\uD83C\uDDF4"),
  BOSNIA_AND_HERZEGOVINA(69, "Bosnia and Herzegovina", "BAM", "\uD83C\uDDE7\uD83C\uDDE6"),
  BRAZIL(9, "Brazil", "BRL", "\uD83C\uDDE7\uD83C\uDDF7"),
  BULGARIA(42, "Bulgaria", "BGN", "\uD83C\uDDE7\uD83C\uDDEC"),
  CANADA(23, "Canada", "CAD", "\uD83C\uDDE8\uD83C\uDDE6"),
  CHILE(64, "Chile", "CLP", "\uD83C\uDDE8\uD83C\uDDF1"),
  CHINA(14, "China", "CNY", "\uD83C\uDDE8\uD83C\uDDF3"),
  COLOMBIA(78, "Colombia", "COP", "\uD83C\uDDE8\uD83C\uDDF4"),
  CROATIA(63, "Croatia", "HRK", "\uD83C\uDDED\uD83C\uDDF7"),
  CUBA(171, "Cuba", "CUP", "\uD83C\uDDE8\uD83C\uDDFA"),
  CYPRUS(82, "Cyprus", "CYP", "\uD83C\uDDE8\uD83C\uDDFE"),
  CZECH_REPUBLIC(34, "Czech Republic", "CZK", "\uD83C\uDDE8\uD83C\uDDFF"),
  DENMARK(55, "Denmark", "DKK", "\uD83C\uDDE9\uD83C\uDDF0"),
  EGYPT(165, "Egypt", "EGP", "\uD83C\uDDEA\uD83C\uDDEC"),
  ESTONIA(70, "Estonia", "EEK", "\uD83C\uDDEA\uD83C\uDDEA"),
  FINLAND(39, "Finland", "FIM", "\uD83C\uDDEB\uD83C\uDDEE"),
  FRANCE(11, "France", "FRF", "\uD83C\uDDEB\uD83C\uDDF7"),
  GEORGIA(168, "Georgia", "GEL", "\uD83C\uDDEC\uD83C\uDDEA"),
  GERMANY(12, "Germany", "DEM", "\uD83C\uDDE9\uD83C\uDDEA"),
  GREECE(44, "Greece", "GRD", "\uD83C\uDDEC\uD83C\uDDF7"),
  HUNGARY(13, "Hungary", "HUF", "\uD83C\uDDED\uD83C\uDDFA"),
  INDIA(48, "India", "INR", "\uD83C\uDDEE\uD83C\uDDF3"),
  INDONESIA(49, "Indonesia", "IDR", "\uD83C\uDDEE\uD83C\uDDE9"),
  IRAN(56, "Iran", "IRR", "\uD83C\uDDEE\uD83C\uDDF7"),
  IRELAND(54, "Ireland", "IEP", "\uD83C\uDDEE\uD83C\uDDEA"),
  ISRAEL(58, "Israel", "ILS", "\uD83C\uDDEE\uD83C\uDDF1"),
  ITALY(10, "Italy", "ITL", "\uD83C\uDDEE\uD83C\uDDF9"),
  JAPAN(45, "Japan", "JPY", "\uD83C\uDDEF\uD83C\uDDF5"),
  LATVIA(71, "Latvia", "LVL", "\uD83C\uDDF1\uD83C\uDDFB"),
  LITHUANIA(72, "Lithuania", "LTL", "\uD83C\uDDF1\uD83C\uDDF9"),
  MALAYSIA(66, "Malaysia", "MYR", "\uD83C\uDDF2\uD83C\uDDFE"),
  MEXICO(26, "Mexico", "MXN", "\uD83C\uDDF2\uD83C\uDDFD"),
  MONTENEGRO(80, "Montenegro", "MEP", "\uD83C\uDDF2\uD83C\uDDEA"),
  NETHERLANDS(31, "Netherlands", "NLG", "\uD83C\uDDF3\uD83C\uDDF1"),
  NEW_ZEALAND(84, "New Zealand", "NZD", "\uD83C\uDDF3\uD83C\uDDFF"),
  NIGERIA(170, "Nigeria", "NGN", "\uD83C\uDDF3\uD83C\uDDEC"),
  NORTH_KOREA(73, "North Korea", "KPW", "\uD83C\uDDF0\uD83C\uDDF5"),
  NORWAY(37, "Norway", "NOK", "\uD83C\uDDF3\uD83C\uDDF4"),
  PAKISTAN(57, "Pakistan", "PKR", "\uD83C\uDDF5\uD83C\uDDF0"),
  PARAGUAY(75, "Paraguay", "PYG", "\uD83C\uDDF5\uD83C\uDDFE"),
  PERU(77, "Peru", "PEN", "\uD83C\uDDF5\uD83C\uDDEA"),
  PHILIPPINES(67, "Philippines", "PHP", "\uD83C\uDDF5\uD83C\uDDED"),
  POLAND(35, "Poland", "PLN", "🇵🇱"),
  PORTUGAL(53, "Portugal", "PTE", "\uD83C\uDDF5\uD83C\uDDF9"),
  REPUBLIC_OF_CHINA_TAIWAN(81, "Republic of China (Taiwan)", "TWD", "\uD83C\uDDF9\uD83C\uDDFC"),
  NORTH_MACEDONIA(79, "North Macedonia", "MKD", "\uD83C\uDDF2\uD83C\uDDF0"),
  REPUBLIC_OF_MOLDOVA(52, "Republic of Moldova", "MDL", "\uD83C\uDDF2\uD83C\uDDE9"),
  ROMANIA(1, "Romania", "RON", "\uD83C\uDDF7\uD83C\uDDF4"),
  RUSSIA(41, "Russia", "RUB", "\uD83C\uDDF7\uD83C\uDDFA"),
  SAUDI_ARABIA(164, "Saudi Arabia", "SAR", "\uD83C\uDDF8\uD83C\uDDE6"),
  SERBIA(65, "Serbia", "RSD", "\uD83C\uDDF7\uD83C\uDDF8"),
  SINGAPORE(68, "Singapore", "SGD", "\uD83C\uDDF8\uD83C\uDDEC"),
  SLOVAKIA(36, "Slovakia", "SKK", "\uD83C\uDDF8\uD83C\uDDF0"),
  SLOVENIA(61, "Slovenia", "SIT", "\uD83C\uDDF8\uD83C\uDDEE"),
  SOUTH_AFRICA(51, "South Africa", "ZAR", "\uD83C\uDDFF\uD83C\uDDE6"),
  SOUTH_KOREA(47, "South Korea", "KRW", "\uD83C\uDDF0\uD83C\uDDF7"),
  SPAIN(15, "Spain", "ESP", "\uD83C\uDDEA\uD83C\uDDF8"),
  SWEDEN(38, "Sweden", "SEK", "\uD83C\uDDF8\uD83C\uDDEA"),
  SWITZERLAND(30, "Switzerland", "CHF", "\uD83C\uDDE8\uD83C\uDDED"),
  THAILAND(59, "Thailand", "THB", "\uD83C\uDDF9\uD83C\uDDED"),
  TURKEY(43, "Turkey", "TRY", "\uD83C\uDDF9\uD83C\uDDF7"),
  UKRAINE(40, "Ukraine", "UAH", "🇺🇦"),
  UNITED_ARAB_EMIRATES(166, "United Arab Emirates", "AED", "\uD83C\uDDE6\uD83C\uDDEA"),
  UNITED_KINGDOM(29, "United Kingdom", "GBP", "\uD83C\uDDEC\uD83C\uDDE7"),
  URUGUAY(74, "Uruguay", "UYU", "\uD83C\uDDFA\uD83C\uDDFE"),
  USA(24, "USA", "USD", "🇺🇸"),
  VENEZUELA(28, "Venezuela", "VEF", "\uD83C\uDDFB\uD83C\uDDEA"),
  UNKNOWN(255, "Unknown", "UNK", "\uD83D\uDE15");

  private final int id;
  private final String readableName;
  private final String currency;
  private final String emoji;

  public static Country getById(Integer id) {
    requireNonNull(id);
    for (Country country : values()) {
      if (Objects.equals(country.id, id)) {
        return country;
      }
    }
    throw new IllegalArgumentException(format("Country with the id %s does not exist", id));
//    return UNKNOWN;
  }

  public static Country getByCurrency(String currency) {
    requireNonNull(currency);
    for (Country country : values()) {
      if (Objects.equals(country.currency, currency)) {
        return country;
      }
    }
    throw new IllegalArgumentException(
      format("Country with the currency %s does not exist", currency));
  }

  public static Country getByEmoji(String emoji) {
    requireNonNull(emoji);
    for (Country country : values()) {
      if (Objects.equals(country.emoji, emoji)) {
        return country;
      }
    }
    throw new IllegalArgumentException(format("Country with the emoji %s does not exist", emoji));
  }

  public static List<Integer> getIDs() {
    return Stream.of(values())
      .map(it -> it.id)
      .collect(Collectors.toList());
  }

  public static List<Country> countries() {
    return List.of(ALBANIA, ARGENTINA, ARMENIA, AUSTRALIA, AUSTRIA, BELARUS, BELGIUM, BOLIVIA,
      BOSNIA_AND_HERZEGOVINA, BRAZIL, BULGARIA, CANADA, CHILE, CHINA, COLOMBIA, CROATIA,
      CUBA, CYPRUS, CZECH_REPUBLIC, DENMARK, EGYPT, ESTONIA, FINLAND, FRANCE, GEORGIA, GERMANY,
      GREECE, HUNGARY, INDIA, INDONESIA, IRAN, IRELAND, ISRAEL, ITALY, JAPAN, LATVIA, LITHUANIA,
      MALAYSIA, MEXICO, MONTENEGRO, NETHERLANDS, NEW_ZEALAND, NIGERIA, NORTH_KOREA, NORWAY,
      PAKISTAN, PARAGUAY, PERU, PHILIPPINES, POLAND, PORTUGAL, REPUBLIC_OF_CHINA_TAIWAN,
      NORTH_MACEDONIA, REPUBLIC_OF_MOLDOVA, ROMANIA, RUSSIA, SAUDI_ARABIA, SERBIA,
      SINGAPORE, SLOVAKIA, SLOVENIA, SOUTH_AFRICA, SOUTH_KOREA, SPAIN, SWEDEN, SWITZERLAND,
      THAILAND, TURKEY, UKRAINE, UNITED_ARAB_EMIRATES, UNITED_KINGDOM, URUGUAY, USA, VENEZUELA);
  }

  public String getUrlName() {
    return readableName
      .replace(' ', '-')
      .replace('(', Character.MIN_VALUE)
      .replace(')', Character.MIN_VALUE);
  }

  public boolean isWorld() {
    return this.equals(WORLD);
  }
}
