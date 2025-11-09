package live.yurii.ebot.deserializer;

import static org.junit.jupiter.api.Assertions.*;

import live.yurii.ebot.TestUtils;
import live.yurii.ebot.dto.BattleConsoleDTO;
import org.junit.jupiter.api.Test;

class BattleConsoleDTODeserializerTest {

  @Test
  void shouldDeserializeJson() throws Exception{
    BattleConsoleDTO dto = TestUtils.readFromFile("/json/battle_console_d11.json", BattleConsoleDTO.class);

    assertEquals(3, dto.getContributions().size());
    assertEquals(0, dto.getMaxPages());
    assertEquals(50, dto.getRounds().size());
  }
}
