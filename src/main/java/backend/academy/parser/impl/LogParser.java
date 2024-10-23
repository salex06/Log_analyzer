package backend.academy.parser.impl;

import backend.academy.log.LogRecord;
import backend.academy.log.LogReport;
import backend.academy.parser.Parser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class LogParser implements Parser {
    @Override
    public LogReport parse(List<Map.Entry<String, Stream<String>>> logRecords, LocalDate fromDate, LocalDate toDate) {
        List<String> files = new ArrayList<>();
        AtomicInteger requestsNumber = new AtomicInteger();
        AtomicLong requestSizeSum = new AtomicLong();
        Map<String, Integer> requestedResources = new HashMap<>();
        Map<Short, Integer> responseCodes = new HashMap<>();
        List<Integer> sizeInBytes = new ArrayList<>();
        logRecords.stream().map(i -> Map.entry(i.getKey(), LogRecord.parseStringStreamToLogRecordStream(i.getValue())))
            .forEach(i -> {
                files.add(i.getKey());
                i.getValue().filter(logRecord -> {
                    LocalDateTime current = logRecord.timeLocal();
                    boolean filterResult = true;
                    if (fromDate != null && current.isBefore(fromDate.atStartOfDay())) {
                        filterResult = false;
                    }
                    if (toDate != null && current.isAfter(toDate.atStartOfDay())) {
                        filterResult = true;
                    }
                    return filterResult;
                }).sorted(new LogRecord.BodySizeInBytesComparator()).forEach(j -> {
                    sizeInBytes.add(j.bodyBytesSent());
                    requestsNumber.getAndIncrement();
                    requestSizeSum.getAndAdd(j.bodyBytesSent());
                    if (requestedResources.containsKey(j.request().requestSource())) {
                        requestedResources.put(j.request().requestSource(),
                            requestedResources.get(j.request().requestSource()) + 1);
                    } else {
                        requestedResources.put(j.request().requestSource(), 1);
                    }
                    if (responseCodes.containsKey(j.status())) {
                        responseCodes.put(j.status(), responseCodes.get(j.status()) + 1);
                    } else {
                        responseCodes.put(j.status(), 1);
                    }
                });
            });

        return new LogReport(
            files,
            fromDate,
            toDate,
            requestsNumber.get(),
            (double) requestSizeSum.get() / requestsNumber.get(),
            sizeInBytes.get((int) (requestsNumber.get() * PERCENTILE)),
            requestedResources, responseCodes
        );
    }

    private static final double PERCENTILE = 0.95;
}
