package backend.academy.format.impl;

import backend.academy.format.Formatter;
import backend.academy.log.LogReport;
import java.util.List;
import java.util.Map;

public class MarkdownFormatter implements Formatter {
    private static final int COL_WIDTH = 10;

    @Override
    public String formatReport(LogReport logReport) {
        StringBuilder stringBuilder = new StringBuilder();
        appendGeneralInformation(stringBuilder, logReport);
        appendSourceInfo(stringBuilder, logReport);
        appendResponceCodesInfo(stringBuilder, logReport);
        return stringBuilder.toString();
    }

    private void appendResponceCodesInfo(StringBuilder stringBuilder, LogReport logReport) {
        stringBuilder.append("#### Коды ответа\n");
        addRow(stringBuilder, List.of("Код", "Имя", "Всего"));
        addRow(stringBuilder, List.of(":" + "-".repeat(COL_WIDTH) + ":", ":" + "-".repeat(COL_WIDTH) + ":",
            ":" + "-".repeat(COL_WIDTH) + ":"));
        for (Map.Entry<Short, Integer> code : logReport.responseCodes().entrySet()) {
            addRow(stringBuilder, List.of(String.valueOf(code.getKey()), LogReport.CODES.get(code.getKey()),
                String.valueOf(code.getValue())));
        }
        stringBuilder.append('\n');
    }

    private void appendSourceInfo(StringBuilder stringBuilder, LogReport logReport) {
        stringBuilder.append("#### Запрашиваемые ресурсы\n");
        addRow(stringBuilder, List.of("Ресурс", "Количество"));
        addRow(stringBuilder, List.of(":" + "-".repeat(COL_WIDTH) + ":", ":" + "-".repeat(COL_WIDTH) + ":"));
        for (Map.Entry<String, Integer> resource : logReport.requestedResources().entrySet()) {
            addRow(stringBuilder, List.of(resource.getKey(), String.valueOf(resource.getValue())));
        }
        stringBuilder.append('\n');
    }

    private void appendGeneralInformation(StringBuilder stringBuilder, LogReport logReport) {
        stringBuilder.append("#### Общая информация\n");
        addRow(stringBuilder, List.of("Метрика", "Значение"));
        addRow(stringBuilder, List.of(":" + "-".repeat(COL_WIDTH) + ":", ":" + "-".repeat(COL_WIDTH) + ":"));
        addRow(stringBuilder, List.of("Файл(-ы)", String.join(", ", logReport.files())));
        addRow(stringBuilder,
            List.of("Начальная дата", (logReport.fromDate() == null ? "-" : logReport.fromDate().toString())));
        addRow(stringBuilder,
            List.of("Конечная дата", (logReport.toDate() == null ? "-" : logReport.toDate().toString())));
        addRow(stringBuilder, List.of("Количество запросов", String.valueOf(logReport.requestsNumber())));
        addRow(stringBuilder, List.of("Средний размер ответа", String.valueOf((int) logReport.requestAverageSize())));
        addRow(stringBuilder, List.of("95p размер ответа", String.valueOf((int) logReport.percentile95())));
        stringBuilder.append('\n');
    }

    private void addRow(StringBuilder stringBuilder, List<String> strings) {
        int colCount = strings.size();
        for (int i = 0; i < colCount; i++) {
            addCol(stringBuilder, strings.get(i), (i == colCount - 1));
        }
        stringBuilder.append('\n');
    }

    private void addCol(StringBuilder stringBuilder, String string, boolean isLast) {
        stringBuilder.append('|').append(string);
        if (isLast) {
            stringBuilder.append('|');
        }
    }
}
