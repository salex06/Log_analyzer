package backend.academy.path.impl;

import backend.academy.path.PathHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalPathHandlerTest {
    private final PathHandler pathHandler = new LocalPathHandler();

    private static List<Map.Entry<String, List<String>>> data() {
        String path1 = "**/*.txt";
        List<String> filesForPath1 = List.of("10_10_24.txt", "30_12_20.txt", "log.txt");
        String path2 = "**/subLogs_1/*.txt";
        List<String> filesForPath2 = List.of("10_10_24.txt", "30_12_20.txt");
        String path3 = "**/subLogs_1/10_10_24.txt";
        List<String> filesForPath3 = List.of("10_10_24.txt");
        String path4 = "logs/*.txt";
        List<String> filesForPath4 = List.of("log.txt");
        String path5 = "logs/subLogs_1/30_12_20.txt";
        List<String> filesForPath5 = List.of("30_12_20.txt");

        return List.of(
            Map.entry(path1, filesForPath1),
            Map.entry(path2, filesForPath2),
            Map.entry(path3, filesForPath3),
            Map.entry(path4, filesForPath4),
            Map.entry(path5, filesForPath5)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    @DisplayName("Ensure handlePath method works correctly")
    void ensureHandlePathWorksCorrectly(Map.Entry<String, List<String>> current) throws Exception {
        String path = current.getKey();
        List<String> expected = current.getValue().stream().sorted().toList();

        List<Map.Entry<String, Stream<String>>> result = pathHandler.handlePath(path);
        List<String> actual = result.stream().map(Map.Entry::getKey).sorted().toList();

        assertEquals(expected, actual);
    }

}
