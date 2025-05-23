package backend.academy.parser.impl;

import backend.academy.filter.impl.LogFilter;
import backend.academy.log.LogRecord;
import backend.academy.log.LogReport;
import backend.academy.parser.Parser;
import com.datadoghq.sketch.ddsketch.DDSketch;
import com.datadoghq.sketch.ddsketch.DDSketches;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The class allows collecting statistics of the log files
 * using Stream API to transform, filter and collect data
 */
public class LogParser implements Parser {
    @Override
    public LogReport parse(
        List<Map.Entry<String, Stream<String>>> logRecords, LocalDate fromDate, LocalDate toDate,
        String filterField, String filterValue
    ) {
        List<String> files = new ArrayList<>();
        AtomicInteger requestsNumber = new AtomicInteger();
        AtomicLong requestSizeSum = new AtomicLong();
        Map<String, Integer> requestedResources = new LinkedHashMap<>();
        Map<Short, Integer> responseCodes = new LinkedHashMap<>();
        DDSketch ddSketch = DDSketches.unboundedDense(RELATIVE_ACCURACY);
        Map<Integer, Integer> numberOfRequestsByHour = new LinkedHashMap<>();
        Map<String, Integer> numberOfRequestsByRemoteAddress = new LinkedHashMap<>();

        logRecords.stream().map(i -> Map.entry(i.getKey(), LogRecord.parseStringStreamToLogRecordStream(i.getValue())))
            .forEach(i -> {
                files.add(i.getKey());
                i.getValue()
                    .filter(
                        logRecord -> (new LogFilter().filter(logRecord, fromDate, toDate, filterField, filterValue)))
                    .sorted(new LogRecord.BodySizeInBytesComparator())
                    .forEach(j -> {
                        ddSketch.accept(j.bodyBytesSent());

                        requestsNumber.getAndIncrement();

                        requestSizeSum.getAndAdd(j.bodyBytesSent());

                        String[] reqSourcePath = j.request().requestSource().split("/");
                        String reqSourceKey = '/' + reqSourcePath[reqSourcePath.length - 1];
                        requestedResources.merge(reqSourceKey, 1, Integer::sum);

                        if (responseCodes.containsKey(j.status())) {
                            responseCodes.put(j.status(), responseCodes.get(j.status()) + 1);
                        } else {
                            responseCodes.put(j.status(), 1);
                        }

                        int reqHour = j.timeLocal().getHour();
                        numberOfRequestsByHour.merge(reqHour, 1, Integer::sum);

                        String remoteAddress = j.remoteAddress();
                        numberOfRequestsByRemoteAddress.merge(remoteAddress, 1, Integer::sum);
                    });
            });

        if (requestsNumber.get() == 0) {
            return null;
        }

        return new LogReport(
            files,
            fromDate,
            toDate,
            requestsNumber.get(),
            (double) requestSizeSum.get() / requestsNumber.get(),
            ddSketch.getValueAtQuantile(PERCENTILE_95),
            requestedResources.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new)),
            responseCodes.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new)),
            numberOfRequestsByHour.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new)),
            numberOfRequestsByRemoteAddress.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(REMOTE_ADDRESS_COUNT_LIMIT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                    LinkedHashMap::new))
        );
    }

    private static final double PERCENTILE_95 = 0.95;
    private static final double RELATIVE_ACCURACY = 0.01;
    private static final int REMOTE_ADDRESS_COUNT_LIMIT = 5;
}
