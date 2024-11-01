package backend.academy.format;

import backend.academy.format.impl.AsciiDocFormatter;
import backend.academy.format.impl.MarkdownFormatter;
import backend.academy.log.LogReport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatHandlerTest {
    private FormatHandler formatHandler;

    private static Formatter[] availableFormatters() {
        return new Formatter[] {new MarkdownFormatter(), new AsciiDocFormatter()};
    }

    @Test
    @DisplayName("Ensure default constructor works correctly")
    void ensureDefaultConstructorWorksCorrectly() {
        formatHandler = new FormatHandler();

        assertThat(formatHandler.formatter()).isInstanceOf(FormatHandler.DEFAULT_FORMATTER.getClass());
    }

    @ParameterizedTest
    @MethodSource("availableFormatters")
    @DisplayName("Ensure formatHandler is initialized correctly")
    void ensureFormatHandlerIsInitializedCorrectly(Formatter formatter) {
        formatHandler = new FormatHandler(formatter);

        assertThat(formatHandler.formatter()).isInstanceOf(formatter.getClass());
    }

    private static List<Map.Entry<Formatter, String>> formatReportData() {
        String ansForMarkdown = """
            #### Общая информация
            |Метрика|Значение|
            |:----------:|:----------:|
            |Файл(-ы)|https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs|
            |Начальная дата|-|
            |Конечная дата|-|
            |Количество запросов|51462|
            |Средний размер ответа|659509|
            |95p размера ответа|1768|
            #### Запрашиваемые ресурсы
            |Ресурс|Количество|
            |:----------:|:----------:|
            |/product_1|30285|
            |/product_2|21104|
            |/product_3|73|
            #### Коды ответа
            |Код|Имя|Всего|
            |:----------:|:----------:|:----------:|
            |404|Not Found|33876|
            |304|Not Modified|13330|
            |200|OK|4028|
            |206|Partial Content|186|
            |403|Forbidden|38|
            |416|Range Not Satisfiable|4|
            #### Количество запросов по часам
            |Часы|Количество за час|
            |:----------:|:----------:|
            |20:00 - 20:59|5600|
            |10:00 - 10:59|1312|
            |08:00 - 08:59|145|
            """;
        String ansForAdoc = """
            ==== Общая информация
            |===
            ^|Метрика >|Значение\s
            ^|Файл(-ы) >|https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs\s
            ^|Начальная дата >|-\s
            ^|Конечная дата >|-\s
            ^|Количество запросов >|51462\s
            ^|Средний размер ответа >|659509\s
            ^|95p размера ответа >|1768\s
            |===
            ==== Запрашиваемые ресурсы
            |===
            ^|Ресурс >|Количество\s
            ^|/product_1 >|30285\s
            ^|/product_2 >|21104\s
            ^|/product_3 >|73\s
            |===
            ==== Коды ответа
            |===
            ^|Код ^|Имя >|Всего\s
            ^|404 ^|Not Found >|33876\s
            ^|304 ^|Not Modified >|13330\s
            ^|200 ^|OK >|4028\s
            ^|206 ^|Partial Content >|186\s
            ^|403 ^|Forbidden >|38\s
            ^|416 ^|Range Not Satisfiable >|4\s
            |===
            ==== Количество запросов по часам
            |===
            ^|Часы >|Количество за час\s
            ^|20:00 - 20:59 >|5600\s
            ^|10:00 - 10:59 >|1312\s
            ^|08:00 - 08:59 >|145\s
            |===
            """;
        return List.of(
            Map.entry(new MarkdownFormatter(), ansForMarkdown),
            Map.entry(new AsciiDocFormatter(), ansForAdoc)
        );
    }

    @ParameterizedTest
    @MethodSource("formatReportData")
    @DisplayName("Ensure formatReport works correctly")
    void ensureFormatReportWorksCorrectly(Map.Entry<Formatter, String> current) {
        formatHandler = new FormatHandler(current.getKey());
        String expected = current.getValue();

        Map<String, Integer> requestResources = new LinkedHashMap<>();
        requestResources.put("/product_1", 30285);
        requestResources.put("/product_2", 21104);
        requestResources.put("/product_3", 73);

        Map<Short, Integer> responseCodes = new LinkedHashMap<>();
        responseCodes.put((short) 404, 33876);
        responseCodes.put((short) 304, 13330);
        responseCodes.put((short) 200, 4028);
        responseCodes.put((short) 206, 186);
        responseCodes.put((short) 403, 38);
        responseCodes.put((short) 416, 4);

        Map<Integer, Integer> requestsByHour = new LinkedHashMap<>();
        requestsByHour.put(20, 5600);
        requestsByHour.put(10, 1312);
        requestsByHour.put(8, 145);

        LogReport logReport = new LogReport(
            List.of(
                "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs"),
            null,
            null,
            51462,
            659509,
            1768,
            requestResources,
            responseCodes,
            requestsByHour
        );

        String actual = formatHandler.formatReport(logReport);

        assertEquals(expected, actual);
    }
}
