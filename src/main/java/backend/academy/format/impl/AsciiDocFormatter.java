package backend.academy.format.impl;

import backend.academy.format.Formatter;
import java.util.List;

/**
 * The class provides methods to format the data using adoc syntax
 */
public class AsciiDocFormatter implements Formatter {
    public static final String TABLE_START_END = "|===";

    @Override
    public String formatTitle(String message) {
        return "==== " + message + '\n';
    }

    @Override
    public String formatTable(List<List<String>> table) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TABLE_START_END).append('\n');
        stringBuilder.append(formatRow(table.getFirst()));
        for (int i = 1; i < table.size(); ++i) {
            stringBuilder.append(formatRow(table.get(i)));
        }
        stringBuilder.append(TABLE_START_END).append('\n');
        return stringBuilder.toString();
    }

    private String formatRow(List<String> rowItems) {
        StringBuilder stringBuilder = new StringBuilder();
        int colCount = rowItems.size();
        for (int i = 0; i < colCount; i++) {
            stringBuilder.append(formatCell(rowItems.get(i) + " ", i, colCount));
        }
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    private String formatCell(String cellItem, int cellIndex, int tableWidth) {
        return (cellIndex == tableWidth - 1 ? ">|" : "^|") + cellItem;
    }

}
