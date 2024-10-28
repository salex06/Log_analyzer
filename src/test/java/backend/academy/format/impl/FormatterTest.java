package backend.academy.format.impl;

import backend.academy.format.Formatter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterTest {
    private Formatter formatter;

    private static List<Map.Entry<Formatter, Map.Entry<String, String>>> testTitleFormattingData() {
        return List.of(
            Map.entry(new AsciiDocFormatter(), Map.entry("message", "==== message\n")),
            Map.entry(new MarkdownFormatter(), Map.entry("message", "#### message\n"))
        );
    }

    @ParameterizedTest
    @MethodSource("testTitleFormattingData")
    @DisplayName("Ensure formatTitle works")
    void ensureFormatTitleMethodWorks(Map.Entry<Formatter, Map.Entry<String, String>> current) {
        formatter = current.getKey();
        String messageToFormat = current.getValue().getKey();
        String expected = current.getValue().getValue();

        String actual = formatter.formatTitle(messageToFormat);

        assertEquals(expected, actual);
    }

    private static List<Map.Entry<Formatter, Map.Entry<List<List<String>>, String>>> testTableFormattingData() {
        List<List<String>> table =
            List.of(List.of("Заголовок 1", "Заголовок 2", "Заголовок 3"), List.of("c1", "c2", "c3"),
                List.of("b1", "b2", "b3"));
        String ansForMarkdown = """
            |Заголовок 1|Заголовок 2|Заголовок 3|
            |:----------:|:----------:|:----------:|
            |c1|c2|c3|
            |b1|b2|b3|
            """;

        String ansForAdoc = """
            |===
            ^|Заголовок 1 ^|Заголовок 2 >|Заголовок 3\s
            ^|c1 ^|c2 >|c3\s
            ^|b1 ^|b2 >|b3\s
            |===
            """;
        return List.of(
            Map.entry(new AsciiDocFormatter(), Map.entry(table, ansForAdoc)),
            Map.entry(new MarkdownFormatter(), Map.entry(table, ansForMarkdown))
        );
    }

    @ParameterizedTest
    @MethodSource("testTableFormattingData")
    @DisplayName("Ensure formatTable works")
    void ensureFormatTableWorks(Map.Entry<Formatter, Map.Entry<List<List<String>>, String>> current) {
        formatter = current.getKey();
        List<List<String>> table = current.getValue().getKey();
        String expected = current.getValue().getValue();

        String actual = formatter.formatTable(table);

        assertEquals(expected, actual);
    }
}
