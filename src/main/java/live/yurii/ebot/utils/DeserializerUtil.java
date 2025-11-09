package live.yurii.ebot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import java.time.Instant;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeserializerUtil {

  public static Instant toInstant(JsonNode jsonNode) {
    if (jsonNode instanceof NullNode) {
      return null;
    }
    return Instant.ofEpochSecond(jsonNode.asLong());
  }

  public static  <T> Stream<T> toStream(Iterator<T> iterator) {
    Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
    return StreamSupport.stream(spliterator, false);
  }

}
