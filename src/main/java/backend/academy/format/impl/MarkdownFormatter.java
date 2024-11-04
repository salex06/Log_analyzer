package backend.academy.format.impl;

import backend.academy.format.Formatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * The class provides methods to format the data using Markdown syntax
 */
public class MarkdownFormatter implements Formatter {
    private static final int COL_WIDTH = 10;

    @Override
    public String formatTitle(String message) {
        return "#### " + message + '\n';
    }

    @Override
    public String formatTable(List<List<String>> table) {
        StringBuilder stringBuilder = new StringBuilder();
        int tableWidth = table.getFirst().size();
        stringBuilder.append(formatRow(table.getFirst()));
        stringBuilder.append(formatRow(
            Stream.iterate((":" + "-".repeat(COL_WIDTH) + ":"), i -> (":" + "-".repeat(COL_WIDTH) + ":"))
                .limit(tableWidth).toList()));
        for (int i = 1; i < table.size(); ++i) {
            stringBuilder.append(formatRow(table.get(i)));
        }
        return stringBuilder.toString();
    }

    private String formatRow(List<String> rowItems) {
        StringBuilder stringBuilder = new StringBuilder();
        int colCount = rowItems.size();
        for (int i = 0; i < colCount; i++) {
            stringBuilder.append(formatCell(rowItems.get(i), i, colCount));
        }
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    private String formatCell(String cellItem, int cellIndex, int tableWidth) {
        String result = '|' + cellItem;
        if (cellIndex == tableWidth - 1) {
            result += '|';
        }
        return result;
    }

}
