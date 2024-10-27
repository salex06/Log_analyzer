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
}
