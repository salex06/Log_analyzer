package backend.academy.parser;

import backend.academy.log.LogReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The class provides operations for collecting statistics of files
 */
public interface Parser {
    /**
     * Collect statistics of log files
     *
     * @param logRecords name of the file and its content represented as stream of strings
     * @param from       the earliest date for a file
     * @param to         the latest date for a file
     * @return {@code LogReport} object that stores the collected statistics
     */
    LogReport parse(List<Map.Entry<String, Stream<String>>> logRecords, LocalDate from, LocalDate to);
}
