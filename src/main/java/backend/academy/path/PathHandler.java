package backend.academy.path;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface PathHandler {
    List<Map.Entry<String, Stream<String>>> handlePath(String path) throws Exception;
}
