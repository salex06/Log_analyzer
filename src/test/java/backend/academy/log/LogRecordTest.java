package backend.academy.log;

import backend.academy.log.LogRecord.Request;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogRecordTest {
    @Test
    @DisplayName("Ensure LogRecord object is initialized correctly")
    void ensureLogRecordObjectIsInitializedCorrectly() {
        String remoteAddress = "23.124.40.255";
        String remoteUser = "-";
        LocalDateTime localDateTime = LocalDateTime.of(2015, 9, 25, 9, 4, 1);
        Request request = new Request("GET", "some/files/1", "HTTP/1.1");
        short status = 200;
        int bodyBytesSent = 500;
        String httpReferer = "-";
        String httpUserAgent = "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)";

        LogRecord logRecord =
            new LogRecord(remoteAddress, remoteUser, localDateTime, request, status, bodyBytesSent, httpReferer,
                httpUserAgent);

        assertEquals(remoteAddress, logRecord.remoteAddress());
        assertEquals(remoteUser, logRecord.remoteUser());
        assertEquals(localDateTime, logRecord.timeLocal());
        assertEquals(request, logRecord.request());
        assertEquals(status, logRecord.status());
        assertEquals(bodyBytesSent, logRecord.bodyBytesSent());
        assertEquals(httpReferer, logRecord.httpReferer());
        assertEquals(httpUserAgent, logRecord.httpUserAgent());
    }

    @Test
    @DisplayName("Ensure the object of the inner Request class is initialized correctly")
    void ensureRequestObjectIsInitializedCorrectly() {
        String expectedType = "POST";
        String expectedResource = "downloads/products/1";
        String expectedProtocol = "HTTP/1.1";

        Request actual = new Request(expectedType, expectedResource, expectedProtocol);

        assertEquals(expectedType, actual.requestType());
        assertEquals(expectedResource, actual.requestSource());
        assertEquals(expectedProtocol, actual.requestHTTP());
    }

    @Test
    @DisplayName("Ensure the correct log is parsed")
    void ensureCorrectLogIsParsed() {
        String log =
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        String expectedRemoteAddress = "93.180.71.3";
        String expectedRemoteUser = "-";
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2015, 5, 17, 8, 5, 32);
        Request expectedRequest = new Request("GET", "/downloads/product_1", "HTTP/1.1");
        short expectedStatus = 304;
        int expectedBodyBytesSent = 0;
        String expectedHttpReferer = "-";
        String expectedHttpUserAgent = "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)";

        LogRecord actual = LogRecord.newLogRecord(log);

        assertEquals(expectedRemoteAddress, actual.remoteAddress());
        assertEquals(expectedRemoteUser, actual.remoteUser());
        assertEquals(expectedLocalDateTime, actual.timeLocal());
        assertEquals(expectedRequest, actual.request());
        assertEquals(expectedStatus, actual.status());
        assertEquals(expectedBodyBytesSent, actual.bodyBytesSent());
        assertEquals(expectedHttpReferer, actual.httpReferer());
        assertEquals(expectedHttpUserAgent, actual.httpUserAgent());
    }

    private static String[] wrongLogsData() {
        return new String[] {
            "- - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "93.180.71.3 - - [17/May/2015:082:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" abs 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\""
        };
    }

    @ParameterizedTest
    @MethodSource("wrongLogsData")
    @DisplayName("Ensure parsing wrong log throws an Exception")
    void ensureParsingWrongLogThrowsAnException(String wrongLog) {
        assertThrows(RuntimeException.class, () -> LogRecord.newLogRecord(wrongLog));
    }

    @Test
    @DisplayName("Ensure parseStringStreamToLogRecordStream works correctly")
    void ensureParseStringStreamToLogRecordStreamWorksCorrectly() {
        Stream<String> log = Stream.of(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");

        String expectedRemoteAddress = "93.180.71.3";
        String expectedRemoteUser = "-";
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2015, 5, 17, 8, 5, 32);
        Request expectedRequest = new Request("GET", "/downloads/product_1", "HTTP/1.1");
        short expectedStatus = 304;
        int expectedBodyBytesSent = 0;
        String expectedHttpReferer = "-";
        String expectedHttpUserAgent = "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)";

        Stream<LogRecord> logRecordStream = LogRecord.parseStringStreamToLogRecordStream(log);

        LogRecord actual = logRecordStream.toList().getFirst();

        assertEquals(expectedRemoteAddress, actual.remoteAddress());
        assertEquals(expectedRemoteUser, actual.remoteUser());
        assertEquals(expectedLocalDateTime, actual.timeLocal());
        assertEquals(expectedRequest, actual.request());
        assertEquals(expectedStatus, actual.status());
        assertEquals(expectedBodyBytesSent, actual.bodyBytesSent());
        assertEquals(expectedHttpReferer, actual.httpReferer());
        assertEquals(expectedHttpUserAgent, actual.httpUserAgent());
    }

    @Test
    @DisplayName("Ensure comparing by body size works correctly")
    void ensureComparingWorksCorrectly() {
        LogRecord log1 = LogRecord.newLogRecord(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\""
        );

        LogRecord log2 = LogRecord.newLogRecord(
            "217.168.17.5 - - [17/May/2015:08:05:34 +0000] \"GET /downloads/product_1 HTTP/1.1\" 200 490 \"-\" \"Debian APT-HTTP/1.3 (0.8.10.3)\""
        );

        List<LogRecord> logRecords = Stream.of(log2, log1).sorted(new LogRecord.BodySizeInBytesComparator()).toList();
        assertThat(logRecords.get(0).bodyBytesSent() < logRecords.get(1).bodyBytesSent()).isTrue();
    }
}
