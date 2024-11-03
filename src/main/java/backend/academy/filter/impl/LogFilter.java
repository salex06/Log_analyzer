package backend.academy.filter.impl;

import backend.academy.filter.Filter;
import backend.academy.log.LogRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

/**
 * The LogFilter class allows to filter the LogRecord objects
 * by date and filter fields
 */
public class LogFilter implements Filter {
    @Override
    public boolean filter(
        LogRecord logRecord,
        LocalDate fromDate,
        LocalDate toDate,
        String filterField,
        String filterValue
    ) {
        return checkDate(logRecord, fromDate, toDate)
            && checkField(logRecord, filterField, filterValue);
    }

    private boolean checkDate(LogRecord logRecord, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime current = logRecord.timeLocal();
        boolean filterResult = true;

        if (fromDate != null && current.isBefore(fromDate.atStartOfDay())) {
            filterResult = false;
        }
        if (toDate != null && current.isAfter(LocalDateTime.of(toDate, END_OF_THE_DAY))) {
            filterResult = false;
        }

        return filterResult;
    }

    private boolean checkField(LogRecord logRecord, String filterFieldAsString, String filterValue) {
        if (filterFieldAsString == null || filterValue == null) {
            return true;
        }
        FilterField filterField = FilterField.valueOf(filterFieldAsString.toUpperCase());
        Pattern filterValuePattern = Pattern.compile(filterValue);
        boolean filterFieldResult = true;
        switch (filterField) {
            case METHOD -> {
                if (!filterValuePattern.matcher(logRecord.request().requestType()).matches()) {
                    filterFieldResult = false;
                }
            }
            case AGENT -> {
                if (!filterValuePattern.matcher(logRecord.httpUserAgent()).matches()) {
                    filterFieldResult = false;
                }
            }
            case REFERER -> {
                if (!filterValuePattern.matcher(logRecord.httpReferer()).matches()) {
                    filterFieldResult = false;
                }
            }
            case REMOTE_USER -> {
                if (!filterValuePattern.matcher(logRecord.remoteUser()).matches()) {
                    filterFieldResult = false;
                }
            }
            case HTTP_VERSION -> {
                if (!filterValuePattern.matcher(logRecord.request().requestHTTP()).matches()) {
                    filterFieldResult = false;
                }
            }
            case REMOTE_ADDRESS -> {
                if (!filterValuePattern.matcher(logRecord.remoteAddress()).matches()) {
                    filterFieldResult = false;
                }
            }
            default -> throw new IllegalArgumentException("Некорректное поле для фильтрации");
        }
        return filterFieldResult;
    }

    public enum FilterField {
        METHOD,
        AGENT,
        REFERER,
        REMOTE_ADDRESS,
        REMOTE_USER,
        HTTP_VERSION;
    }

    private static final LocalTime END_OF_THE_DAY =
        LocalTime.of(LocalTime.MAX.getHour(), LocalTime.MAX.getMinute(), LocalDateTime.MAX.getSecond());
}
