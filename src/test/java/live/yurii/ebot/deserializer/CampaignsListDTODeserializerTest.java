package live.yurii.ebot.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import live.yurii.ebot.TestUtils;
import live.yurii.ebot.dto.CampaignsListDTO;
import org.junit.jupiter.api.Test;

class CampaignsListDTODeserializerTest {

  @Test
  void shouldDeserializeJson() throws Exception {
    CampaignsListDTO campaignsList = TestUtils.readFromFile("/json/list.json", CampaignsListDTO.class);

    assertEquals(266, campaignsList.battles().size());
    assertEquals(74, campaignsList.countries().size());
  }
}
