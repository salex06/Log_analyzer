package backend.academy.parser;

import backend.academy.log.LogReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface Parser {
    LogReport parse(List<Map.Entry<String, Stream<String>>> logRecords, LocalDate from, LocalDate to);
}
