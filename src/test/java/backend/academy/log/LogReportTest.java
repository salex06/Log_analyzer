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
    private static final Map<Integer, Integer> expectedRequestsNumberByHour = new LinkedHashMap<>();
    private static final Map<String, Integer> expectedRequestsNumberByRemoteAddress = new LinkedHashMap<>();

    private static LogReport logReport;

    @BeforeAll
    static void setup() {
        expectedRequestedResources.put("/product_1", 1);
        expectedRequestedResources.put("/product_2", 2);

        expectedResponseCodes.put((short) 200, 1);
        expectedResponseCodes.put((short) 304, 1);
        expectedResponseCodes.put((short) 404, 1);

        expectedRequestsNumberByHour.put(11, 2);
        expectedRequestsNumberByHour.put(8, 1);
        expectedRequestsNumberByHour.put(12, 1);
        expectedRequestsNumberByHour.put(15, 1);

        expectedRequestsNumberByRemoteAddress.put("123.45.32.200", 5);
        expectedRequestsNumberByRemoteAddress.put("201.106.110.21", 3);

        logReport = new LogReport(expectedFiles, expectedFromDate, expectedToDate, expectedRequestNumber,
            expectedRequestAverageSize, expectedPercentile95, expectedRequestedResources, expectedResponseCodes,
            expectedRequestsNumberByHour, expectedRequestsNumberByRemoteAddress);
    }

    @Test
    @DisplayName("Ensure LogReport object is initialized correctly")
    void ensureLogReportIsInitializedCorrectly() {
        assertEquals(expectedFiles, logReport.files());
        assertEquals(expectedFromDate, logReport.fromDate());
        assertEquals(expectedToDate, logReport.toDate());
        assertEquals(expectedRequestNumber, logReport.requestsNumber());
        assertEquals(expectedRequestAverageSize, logReport.requestAverageSize());
        assertEquals(expectedPercentile95, (int) logReport.percentile95());
        assertEquals(expectedRequestedResources, logReport.requestedResources());
        assertEquals(expectedResponseCodes, logReport.responseCodes());
        assertEquals(expectedRequestsNumberByHour, logReport.requestsNumberByHour());
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

        List<List<String>> actual = logReport.getGeneralInfoAsTable();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Ensure getRequestsNumberByHourAsTable works correctly")
    void ensureGetRequestsNumberByHourAsTableWorks() {
        List<List<String>> expected = List.of(
            List.of("Часы", "Количество за час"),
            List.of("11:00 - 11:59", "2"),
            List.of("08:00 - 08:59", "1"),
            List.of("12:00 - 12:59", "1"),
            List.of("15:00 - 15:59", "1")
        );

        List<List<String>> actual = logReport.getRequestsNumberByHourAsTable();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Ensure getRequestsNumberByRemoteAddressAsTable works correctly")
    void ensureGetRequestsNumberByRemoteAddressAsTableWorks() {
        List<List<String>> expected = List.of(
            List.of("Адрес пользователя", "Количество запросов с адреса"),
            List.of("123.45.32.200", "5"),
            List.of("201.106.110.21", "3")
        );

        List<List<String>> actual = logReport.getRequestsNumberByRemoteAddressAsTable();

        assertEquals(expected, actual);
    }

}
