package backend.academy.log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The class presents a report on processed log files.
 * The report contains statistics: general information (number of files, size, etc.),
 * information about the requested resources, information about the server response
 *
 * @param files                processed log files
 * @param fromDate             the earliest log file
 * @param toDate               the latest log file
 * @param requestsNumber       the number of requests
 * @param requestAverageSize   the average size of all the requests
 * @param percentile95         95% of requests are smaller or equal in size, and 5% of requests are larger or equal
 * @param requestedResources   the storage of the requested resources
 * @param responseCodes        server response storage
 * @param requestsNumberByHour the number of requests during certain hours
 */
@SuppressWarnings("RecordComponentNumber")
public record LogReport(
    List<String> files,
    LocalDate fromDate,
    LocalDate toDate,
    int requestsNumber,
    double requestAverageSize,
    double percentile95,
    Map<String, Integer> requestedResources,
    Map<Short, Integer> responseCodes,
    Map<Integer, Integer> requestsNumberByHour
) {

    public List<List<String>> getRequestsNumberByHourAsTable() {
        List<List<String>> table = new ArrayList<>(responseCodes.size());
        table.add(List.of("Часы", "Количество за час"));
        for (Map.Entry<Integer, Integer> row : requestsNumberByHour.entrySet()) {
            table.add(List.of(
                LocalTime.of(row.getKey(), LocalTime.MIN.getMinute()).toString()
                    + " - "
                    + LocalTime.of(row.getKey(), LocalTime.MAX.getMinute()).toString(),
                row.getValue().toString()));
        }
        return table;
    }

    /**
     * Allows getting info about response codes in the form of a table
     *
     * @return table contains info about response codes
     */
    public List<List<String>> getResponseCodesAsTable() {
        List<List<String>> table = new ArrayList<>(responseCodes.size());
        table.add(List.of("Код", "Имя", "Всего"));
        for (Map.Entry<Short, Integer> row : responseCodes.entrySet()) {
            table.add(List.of(String.valueOf(row.getKey()), CODES.get(row.getKey()), String.valueOf(row.getValue())));
        }
        return table;
    }

    /**
     * Allows getting info about requested resources in the form of a table
     *
     * @return table contains info about requested resources
     */
    public List<List<String>> getResourcesAsTable() {
        List<List<String>> table = new ArrayList<>(requestedResources.size());
        table.add(List.of("Ресурс", "Количество"));
        for (Map.Entry<String, Integer> row : requestedResources.entrySet()) {
            table.add(List.of(row.getKey(), String.valueOf(row.getValue())));
        }
        return table;
    }

    /**
     * Allows getting general info in the form of a table
     *
     * @return table contains general info
     */
    public List<List<String>> getGeneralInfoAsTable() {
        List<List<String>> table = new ArrayList<>();
        table.add(List.of("Метрика", "Значение"));
        table.add(List.of("Файл(-ы)", String.join(", ", files)));
        table.add(List.of("Начальная дата", (fromDate() == null ? "-" : fromDate().toString())));
        table.add(List.of("Конечная дата", (toDate() == null ? "-" : toDate().toString())));
        table.add(List.of("Количество запросов", String.valueOf(requestsNumber)));
        table.add(List.of("Средний размер ответа", String.valueOf((int) requestAverageSize)));
        table.add(List.of("95p размера ответа", String.valueOf((int) percentile95)));
        return table;
    }

    /**
     * Storage of HTTP codes and its names
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static final Map<Short, String> CODES = Map.<Short, String>ofEntries(
        Map.entry((short) 100, "Continue"),
        Map.entry((short) 101, "Switching protocols"),
        Map.entry((short) 102, "Processing"),
        Map.entry((short) 103, "Early Hints"),
        Map.entry((short) 200, "OK"),
        Map.entry((short) 201, "Created"),
        Map.entry((short) 202, "Accepted"),
        Map.entry((short) 204, "No Content"),
        Map.entry((short) 205, "Reset Content"),
        Map.entry((short) 206, "Partial Content"),
        Map.entry((short) 207, "Multi-Status"),
        Map.entry((short) 208, "Already Reported"),
        Map.entry((short) 226, "IM Used"),
        Map.entry((short) 300, "Multiple Choices"),
        Map.entry((short) 301, "Moved Permanently"),
        Map.entry((short) 302, "Found"),
        Map.entry((short) 303, "See Other"),
        Map.entry((short) 304, "Not Modified"),
        Map.entry((short) 305, "Use Proxy"),
        Map.entry((short) 307, "Temporary Redirect"),
        Map.entry((short) 308, "Permanent Redirect"),
        Map.entry((short) 400, "Bad Request"),
        Map.entry((short) 401, "Unauthorized"),
        Map.entry((short) 402, "Payment Required"),
        Map.entry((short) 403, "Forbidden"),
        Map.entry((short) 404, "Not Found"),
        Map.entry((short) 405, "Method Not Allowed"),
        Map.entry((short) 406, "Not Acceptable"),
        Map.entry((short) 407, "Proxy Authentication"),
        Map.entry((short) 408, "Request Timeout"),
        Map.entry((short) 409, "Conflict"),
        Map.entry((short) 410, "Gone"),
        Map.entry((short) 411, "Length Required"),
        Map.entry((short) 412, "Precondition Failed"),
        Map.entry((short) 413, "Payload Too Large"),
        Map.entry((short) 414, "URI Too Long"),
        Map.entry((short) 415, "Unsupported Media Type"),
        Map.entry((short) 416, "Range Not Satisfiable"),
        Map.entry((short) 417, "Expectation Failed"),
        Map.entry((short) 418, "I’m a teapot"),
        Map.entry((short) 419, "Authentication Timeout"),
        Map.entry((short) 421, "Misdirected Request"),
        Map.entry((short) 422, "Unprocessable Entity"),
        Map.entry((short) 423, "Locked"),
        Map.entry((short) 424, "Failed Dependency"),
        Map.entry((short) 425, "Too Early"),
        Map.entry((short) 426, "Upgrade Required"),
        Map.entry((short) 428, "Precondition Required"),
        Map.entry((short) 429, "Too Many Requests"),
        Map.entry((short) 431, "Request Header Fields Too Large"),
        Map.entry((short) 449, "Retry With"),
        Map.entry((short) 451, "Unavailable For Legal Reasons"),
        Map.entry((short) 499, "Client Closed Request"),
        Map.entry((short) 500, "Internal Server Error"),
        Map.entry((short) 501, "Not Implemented"),
        Map.entry((short) 502, "Bad Gateway"),
        Map.entry((short) 503, "Service Unavailable"),
        Map.entry((short) 504, "Gateway Timeout"),
        Map.entry((short) 505, "HTTP Version Not Supported"),
        Map.entry((short) 506, "Variant Also Negotiates"),
        Map.entry((short) 507, "Insufficient Storage"),
        Map.entry((short) 508, "Loop Detected"),
        Map.entry((short) 509, "Bandwidth Limit Exceeded"),
        Map.entry((short) 510, "Not Extended"),
        Map.entry((short) 511, "Network Authentication Required"),
        Map.entry((short) 520, "Unknown Error"),
        Map.entry((short) 521, "Web Server Is Down"),
        Map.entry((short) 522, "Connection Timed Out"),
        Map.entry((short) 523, "Origin Is Unreachable"),
        Map.entry((short) 524, "A Timeout Occurred"),
        Map.entry((short) 525, "SSL Handshake Failed"),
        Map.entry((short) 526, "Invalid SSL Certificate")
    );
}
