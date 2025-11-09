package live.yurii.ebot.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import live.yurii.ebot.exception.NotAuthorizedException;
import live.yurii.ebot.exception.TooManyRequestsException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class AbstractDeserializer<T> extends JsonDeserializer<T> {

  private static final String TOO_MANY_REQUESTS = "{\"error\":\"Too many requests\"}";
  private static final String NOT_AUTHORIZED = "{\"error\":\"not_authorized\"}";

  private void checkForErrors(String json) {
    if (json.equals(TOO_MANY_REQUESTS)) {
      throw new TooManyRequestsException("We must slow down :(");
    }
    if (json.equals(NOT_AUTHORIZED)) {
      throw new NotAuthorizedException("Not authorized :(");
    }
  }

  @Override
  public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    checkForErrors(node.toString());
    return deserialize(node);
  }

  protected abstract T deserialize(JsonNode node) throws IOException;
}
