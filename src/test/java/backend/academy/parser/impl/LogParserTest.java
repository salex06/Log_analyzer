package backend.academy.parser.impl;

import backend.academy.log.LogReport;
import backend.academy.parser.Parser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogParserTest {
    private final Parser logParser = new LogParser();
    private final Path pathToFile = Path.of("src/test/java/backend/academy/parser/impl/testFile.txt");
    private final Map.Entry<String, Stream<String>> inputStream =
        Map.entry(pathToFile.getFileName().toString(), Files.lines(pathToFile));

    LogParserTest() throws IOException {
    }

    @Test
    @DisplayName("Ensure the parse method works correctly if there are no 'from' and 'to' flags")
    void ensureParsingWorksCorrectlyIfNoFromAndToDate() {
        List<String> expectedFiles = List.of("testFile.txt");
        LocalDate expectedFromDate = null;
        LocalDate expectedToDate = null;
        Integer expectedRequestNumber = 5;
        Double expectedRequestAverageSize = 17124002.8;
        Integer expectedPercentile95 = 487;
        Map<String, Integer> expectedRequestedResources = new HashMap<>();
        expectedRequestedResources.put("/product_1", 3);
        expectedRequestedResources.put("/product_2", 2);
        Map<Short, Integer> expectedResponseCodes = new HashMap<>();
        expectedResponseCodes.put((short) 200, 2);
        expectedResponseCodes.put((short) 304, 2);
        expectedResponseCodes.put((short) 404, 1);

        LogReport actual = logParser.parse(List.of(inputStream), expectedFromDate, expectedToDate);

        assertEquals(expectedFiles, actual.files());
        assertEquals(expectedFromDate, actual.fromDate());
        assertEquals(expectedToDate, actual.toDate());
        assertEquals(expectedRequestNumber, actual.requestsNumber());
        assertEquals(expectedRequestAverageSize, actual.requestAverageSize());
        assertEquals(expectedPercentile95, (int) actual.percentile95());
        assertEquals(expectedRequestedResources, actual.requestedResources());
        assertEquals(expectedResponseCodes, actual.responseCodes());
    }

    @Test
    @DisplayName("Ensure the parse method works correctly if there are no 'to' flag")
    void ensureParsingWorksCorrectlyIfNoToDate() {
        List<String> expectedFiles = List.of("testFile.txt");
        LocalDate expectedFromDate = LocalDate.of(2015, 5, 29);
        LocalDate expectedToDate = null;
        Integer expectedRequestNumber = 4;
        Double expectedRequestAverageSize = 21404881.0;
        Integer expectedPercentile95 = 321;
        Map<String, Integer> expectedRequestedResources = new HashMap<>();
        expectedRequestedResources.put("/product_1", 2);
        expectedRequestedResources.put("/product_2", 2);
        Map<Short, Integer> expectedResponseCodes = new HashMap<>();
        expectedResponseCodes.put((short) 200, 1);
        expectedResponseCodes.put((short) 304, 2);
        expectedResponseCodes.put((short) 404, 1);

        LogReport actual = logParser.parse(List.of(inputStream), expectedFromDate, expectedToDate);

        assertEquals(expectedFiles, actual.files());
        assertEquals(expectedFromDate, actual.fromDate());
        assertEquals(expectedToDate, actual.toDate());
        assertEquals(expectedRequestNumber, actual.requestsNumber());
        assertEquals(expectedRequestAverageSize, actual.requestAverageSize());
        assertEquals(expectedPercentile95, (int) actual.percentile95());
        assertEquals(expectedRequestedResources, actual.requestedResources());
        assertEquals(expectedResponseCodes, actual.responseCodes());
    }

    @Test
    @DisplayName("Ensure the parse method works correctly if there are no 'from' flag")
    void ensureParsingWorksCorrectlyIfNoFromDate() {
        List<String> expectedFiles = List.of("testFile.txt");
        LocalDate expectedFromDate = null;
        LocalDate expectedToDate = LocalDate.of(2015, 5, 29);
        Integer expectedRequestNumber = 4;
        Double expectedRequestAverageSize = 21405003.5;
        Integer expectedPercentile95 = 487;
        Map<String, Integer> expectedRequestedResources = new HashMap<>();
        expectedRequestedResources.put("/product_1", 2);
        expectedRequestedResources.put("/product_2", 2);
        Map<Short, Integer> expectedResponseCodes = new HashMap<>();
        expectedResponseCodes.put((short) 200, 2);
        expectedResponseCodes.put((short) 304, 1);
        expectedResponseCodes.put((short) 404, 1);

        LogReport actual = logParser.parse(List.of(inputStream), expectedFromDate, expectedToDate);

        assertEquals(expectedFiles, actual.files());
        assertEquals(expectedFromDate, actual.fromDate());
        assertEquals(expectedToDate, actual.toDate());
        assertEquals(expectedRequestNumber, actual.requestsNumber());
        assertEquals(expectedRequestAverageSize, actual.requestAverageSize());
        assertEquals(expectedPercentile95, (int) actual.percentile95());
        assertEquals(expectedRequestedResources, actual.requestedResources());
        assertEquals(expectedResponseCodes, actual.responseCodes());
    }

    @Test
    @DisplayName("Ensure the parse method works correctly if there are 'from' and 'to' flags")
    void ensureParsingWorksCorrectlyIfThereAreFromAndToDates() {
        List<String> expectedFiles = List.of("testFile.txt");
        LocalDate expectedFromDate = LocalDate.of(2015, 5, 29);
        LocalDate expectedToDate = LocalDate.of(2015, 5, 29);
        Integer expectedRequestNumber = 3;
        Double sumOfSizes = 85619524.0;
        Double expectedRequestAverageSize = sumOfSizes / expectedRequestNumber;
        Integer expectedPercentile95 = 321;
        Map<String, Integer> expectedRequestedResources = new HashMap<>();
        expectedRequestedResources.put("/product_1", 1);
        expectedRequestedResources.put("/product_2", 2);
        Map<Short, Integer> expectedResponseCodes = new HashMap<>();
        expectedResponseCodes.put((short) 200, 1);
        expectedResponseCodes.put((short) 304, 1);
        expectedResponseCodes.put((short) 404, 1);

        LogReport actual = logParser.parse(List.of(inputStream), expectedFromDate, expectedToDate);

        assertEquals(expectedFiles, actual.files());
        assertEquals(expectedFromDate, actual.fromDate());
        assertEquals(expectedToDate, actual.toDate());
        assertEquals(expectedRequestNumber, actual.requestsNumber());
        assertEquals(expectedRequestAverageSize, actual.requestAverageSize());
        assertEquals(expectedPercentile95, (int) actual.percentile95());
        assertEquals(expectedRequestedResources, actual.requestedResources());
        assertEquals(expectedResponseCodes, actual.responseCodes());
    }
}
