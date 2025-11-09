package live.yurii.ebot;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T readFromFile(String relativePath, Class<T> clazz) throws IOException {
    Path path = Paths.get("src/test/resources" + relativePath);
    return objectMapper.readValue(Files.readString(path), clazz);
  }
}
