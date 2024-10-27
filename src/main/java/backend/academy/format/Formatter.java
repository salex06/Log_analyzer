package backend.academy.format;

import java.util.List;

/**
 * The interface provides methods to format the
 * title and table with the specified type of formatting
 */
public interface Formatter {
    /**
     * Formats the message as a title
     *
     * @param message data to be formatted
     * @return {@code String} - formatted data
     */
    String formatTitle(String message);

    /**
     * Formats the table according to a specific type of formatting
     *
     * @param table table representation in the system
     * @return {@code String} - formatted table
     */
    String formatTable(List<List<String>> table);

    /**
     * Formats list of strings as a part of the table
     *
     * @param rowItems list of strings - parts of the table
     * @return {@code String} - formatted row
     */
    String formatRow(List<String> rowItems);

    /**
     * Formats the string as a cell in the table
     *
     * @param cellItem   value of the cell
     * @param cellIndex  index of the cell
     * @param tableWidth width of the table
     * @return {@code String} - formatted row
     */
    String formatCell(String cellItem, int cellIndex, int tableWidth);
}
