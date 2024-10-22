package backend.academy.log;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record LogReport(
    List<String> files,
    LocalDate fromDate,
    LocalDate toDate,
    int requestsNumber,
    double requestAverageSize,
    double percentile95,
    Map<String, Integer> requestedResources,
    Map<Short, Integer> responseCodes
) {

    @SuppressWarnings("checkstyle:magicnumber")
    public static final Map<Short, String> CODES = Stream.of(
        new AbstractMap.SimpleImmutableEntry<>((short) 200, "OK"),
        new AbstractMap.SimpleImmutableEntry<>((short) 301, "Moved permanently"),
        new AbstractMap.SimpleImmutableEntry<>((short) 302, "Found"),
        new AbstractMap.SimpleImmutableEntry<>((short) 304, "Not modified"),
        new AbstractMap.SimpleImmutableEntry<>((short) 401, "Unauthorized"),
        new AbstractMap.SimpleImmutableEntry<>((short) 403, "Forbidden"),
        new AbstractMap.SimpleImmutableEntry<>((short) 404, "Not found"),
        new AbstractMap.SimpleImmutableEntry<>((short) 500, "Internal server error"),
        new AbstractMap.SimpleImmutableEntry<>((short) 504, "Gateway timeout")
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}
