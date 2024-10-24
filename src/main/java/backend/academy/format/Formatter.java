package backend.academy.format;

import java.util.List;

public interface Formatter {
    String formatTitle(String message);

    String formatTable(List<List<String>> table);

    String formatRow(List<String> rowItems);

    String formatCell(String cellItem, int cellIndex, int tableWidth);
}
