package backend.academy.filter.impl;

import backend.academy.filter.Filter;
import backend.academy.log.LogRecord;
import backend.academy.log.LogRecord.Request;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogFilterTest {
    private static final Filter logFilter = new LogFilter();
    private static LogRecord logRecord;

    @BeforeAll
    static void setup() {
        logRecord = Mockito.mock(LogRecord.class);
        Mockito.when(logRecord.remoteAddress()).thenReturn("93.180.71.3");
        Mockito.when(logRecord.remoteUser()).thenReturn("-");
        Mockito.when(logRecord.timeLocal()).thenReturn(LocalDateTime.of(2015, 5, 17, 8, 5, 32));
        Mockito.when(logRecord.request()).thenReturn(new Request("GET", "/downloads/product_1", "HTTP/1.1"));
        Mockito.when(logRecord.status()).thenReturn((short) 304);
        Mockito.when(logRecord.bodyBytesSent()).thenReturn(0);
        Mockito.when(logRecord.httpReferer()).thenReturn("-");
        Mockito.when(logRecord.httpUserAgent()).thenReturn("Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)");
    }

    @ParameterizedTest
    @CsvSource({
        "2010, 2, 20, 2017, 3, 10, method, GET",
        "2015, 5, 17, 2016, 3, 10, method, GET",
        "2010, 5, 17, 2015, 5, 17, method, GET",
        "2015, 3, 10, 2015, 12, 30, agent, .*ubuntu.*",
        "2015, 3, 10, 2015, 12, 30, agent, .*ubuntu.*\\.21.*",
        "2015, 3, 10, 2015, 12, 30, agent, Debian.*",
        "2015, 3, 10, 2015, 12, 30, remote_address, 93\\.180\\.71\\.3",
        "2015, 3, 10, 2015, 12, 30, referer, -",
        "2015, 3, 10, 2015, 12, 30, http_version, .*1\\.1.*",
    })
    @DisplayName("Ensure logRecord is filtered correctly")
    void ensureLogRecordIsFiltered(
        int fromYear, int fromMonth, int fromDay,
        int toYear, int toMonth, int toDay, String field, String value
    ) {
        LocalDate fromDate = LocalDate.of(fromYear, fromMonth, fromDay);
        LocalDate toDate = LocalDate.of(toYear, toMonth, toDay);

        boolean actual = logFilter.filter(logRecord, fromDate, toDate, field, value);

        assertTrue(actual);
    }

    @ParameterizedTest
    @CsvSource({
        "2015, 6, 20, 2017, 3, 10, method, GET",
        "2014, 2, 20, 2015, 3, 10, method, GET",
        "2010, 2, 20, 2017, 3, 10, method, HEAD",
        "2010, 2, 20, 2017, 3, 10, agent, Mozilla.*",
        "2010, 2, 20, 2017, 3, 10, remote_address, 10\\.10\\.10\\.10",
        "2010, 2, 20, 2017, 3, 10, referer, \\+",
        "2010, 2, 20, 2017, 3, 10, http_version, .*1\\.2.*",

    })
    @DisplayName("Ensure logRecord is not filtered")
    void ensureLogRecordIsNotFiltered(
        int fromYear, int fromMonth, int fromDay,
        int toYear, int toMonth, int toDay, String field, String value
    ) {
        LocalDate fromDate = LocalDate.of(fromYear, fromMonth, fromDay);
        LocalDate toDate = LocalDate.of(toYear, toMonth, toDay);

        boolean actual = logFilter.filter(logRecord, fromDate, toDate, field, value);

        assertFalse(actual);
    }

    @Test
    @DisplayName("Ensure filter method throws exception if there are wrong filterField")
    void ensureMethodThrowsExceptionIfWrongFilterField() {
        String filterField = "wrong";

        assertThrows(IllegalArgumentException.class,
            () -> logFilter.filter(logRecord, null, null, filterField, "value"));
    }
}
