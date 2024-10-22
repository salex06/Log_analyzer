package backend.academy.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public record LogRecord(String remoteAddress, String remoteUser, LocalDateTime timeLocal, Request request, short status,
                        int bodyBytesSent, String httpReferer, String httpUserAgent) {

    private static final byte REMOTE_ADDRESS_GROUP_NUMBER = 1;
    private static final byte REMOTE_USER_GROUP_NUMBER = 2;
    private static final byte TIME_GROUP_NUMBER = 3;
    private static final byte REQUEST_GROUP_NUMBER = 4;
    private static final byte STATUS_GROUP_NUMBER = 5;
    private static final byte BODY_SIZE_GROUP_NUMBER = 6;
    private static final byte HTTP_REFERER_GROUP_NUMBER = 7;
    private static final byte HTTP_USER_AGENT_GROUP_NUMBER = 8;

    public static LogRecord newLogRecord(String log) {
        Matcher matcher =
            Pattern.compile("^(.+) - ([^\\[]+) \\[(.+)\\] \"(.+)\" (\\d+) (\\d+) \"(.+)\" \"(.+)\"$")
                .matcher(log);

        if (!matcher.matches()) {
            throw new RuntimeException("Parsing log record error! " + log);
        }

        String remAddr = matcher.group(REMOTE_ADDRESS_GROUP_NUMBER);
        String remUser = matcher.group(REMOTE_USER_GROUP_NUMBER);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
        LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(TIME_GROUP_NUMBER), formatter);
        String request = matcher.group(REQUEST_GROUP_NUMBER);
        short status = Short.parseShort(matcher.group(STATUS_GROUP_NUMBER));
        int bodyBytesSent = Integer.parseInt(matcher.group(BODY_SIZE_GROUP_NUMBER));
        String httpReferer = matcher.group(HTTP_REFERER_GROUP_NUMBER);
        String httpUserAgent = matcher.group(HTTP_USER_AGENT_GROUP_NUMBER);

        String[] requestData = request.split(" ");

        String requestType = requestData[0];
        String requestSource = requestData[1];
        String requestHTTP = requestData[2];

        return new LogRecord(
            remAddr,
            remUser,
            localDateTime,
            new Request(requestType, requestSource, requestHTTP),
            status,
            bodyBytesSent,
            httpReferer,
            httpUserAgent
        );
    }

    public static Stream<LogRecord> parseStringStreamToLogRecordStream(Stream<String> stringStream) {
        return stringStream.map(LogRecord::newLogRecord);
    }

    public record Request(String requestType, String requestSource, String requestHTTP) {

    }
}
