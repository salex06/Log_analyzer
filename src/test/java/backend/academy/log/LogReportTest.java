package backend.academy.log;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogReportTest {
    private static final List<String> expectedFiles = List.of("testFile.txt");
    private static final LocalDate expectedFromDate = LocalDate.of(2015, 5, 29);
    private static final LocalDate expectedToDate = LocalDate.of(2015, 5, 29);
    private static final Integer expectedRequestNumber = 3;
    private static final Double sumOfSizes = 85619524.0;
    private static final double expectedRequestAverageSize = (int) (sumOfSizes / expectedRequestNumber);
    private static final double expectedPercentile95 = 85619205;
    private static final Map<String, Integer> expectedRequestedResources = new LinkedHashMap<>();
    private static final Map<Short, Integer> expectedResponseCodes = new LinkedHashMap<>();

    @BeforeAll
    static void setup() {
        expectedRequestedResources.put("/product_1", 1);
        expectedRequestedResources.put("/product_2", 2);
        expectedResponseCodes.put((short) 200, 1);
        expectedResponseCodes.put((short) 304, 1);
        expectedResponseCodes.put((short) 404, 1);
    }

    @Test
    @DisplayName("Ensure LogReport object is initialized correctly")
    void ensureLogReportIsInitializedCorrectly() {
        LogReport actual = new LogReport(expectedFiles, expectedFromDate, expectedToDate, expectedRequestNumber,
            expectedRequestAverageSize, expectedPercentile95, expectedRequestedResources, expectedResponseCodes);

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
    @DisplayName("Ensure getResponseCodesAsTable works correctly")
    void ensureGetResponseCodesAsTableWorks() {
        List<List<String>> expected = List.of(
            List.of("Код", "Имя", "Всего"),
            List.of("200", "OK", "1"),
            List.of("304", "Not Modified", "1"),
            List.of("404", "Not Found", "1")
        );

        LogReport logReport = new LogReport(expectedFiles, expectedFromDate, expectedToDate, expectedRequestNumber,
            expectedRequestAverageSize, expectedPercentile95, expectedRequestedResources, expectedResponseCodes);

        List<List<String>> actual = logReport.getResponseCodesAsTable();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Ensure getResourcesAsTable works correctly")
    void ensureGetResourcesAsTableWorks() {
        List<List<String>> expected = List.of(
            List.of("Ресурс", "Количество"),
            List.of("/product_1", "1"),
            List.of("/product_2", "2")
        );

        LogReport logReport = new LogReport(expectedFiles, expectedFromDate, expectedToDate, expectedRequestNumber,
            expectedRequestAverageSize, expectedPercentile95, expectedRequestedResources, expectedResponseCodes);

        List<List<String>> actual = logReport.getResourcesAsTable();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Ensure getGeneralInfoAsTable works correctly")
    void ensureGetGeneralInfoAsTableWorks() {
        List<List<String>> expected = List.of(
            List.of("Метрика", "Значение"),
            List.of("Файл(-ы)", expectedFiles.getFirst()),
            List.of("Начальная дата", expectedFromDate.toString()),
            List.of("Конечная дата", expectedToDate.toString()),
            List.of("Количество запросов", String.valueOf(expectedRequestNumber)),
            List.of("Средний размер ответа", String.valueOf((int) expectedRequestAverageSize)),
            List.of("95p размера ответа", String.valueOf((int) expectedPercentile95))
        );

        LogReport logReport = new LogReport(expectedFiles, expectedFromDate, expectedToDate, expectedRequestNumber,
            expectedRequestAverageSize, expectedPercentile95, expectedRequestedResources, expectedResponseCodes);

        List<List<String>> actual = logReport.getGeneralInfoAsTable();

        assertEquals(expected, actual);
    }
}
