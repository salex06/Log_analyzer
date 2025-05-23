package backend.academy.log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * The class represents a log model
 * that stores information about the request
 *
 * @param remoteAddress the IP address of the client making the request.
 * @param remoteUser    HTTP Authenticated User. When the user's name is not set, this field shows -.
 * @param timeLocal     local server time.
 * @param request       Request record object. The request type, path and protocol.
 * @param status        the server response code
 * @param bodyBytesSent the size of server response in bytes.
 * @param httpReferer   the URL of the referral.
 * @param httpUserAgent the user agent of the client (web browser).
 */
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

    /**
     * Creates new LogRecord instance by parsing given string
     *
     * @param log log in the form of a string
     * @return {@code LogRecord} object
     */
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

    /**
     * Converts stream of logs in the form of strings to stream of LogRecord objects
     *
     * @param stringStream stream of logs in the form of strings
     * @return {@code Stream<LogRecord>} - stream of LogRecord objects
     */
    public static Stream<LogRecord> parseStringStreamToLogRecordStream(Stream<String> stringStream) {
        return stringStream.map(LogRecord::newLogRecord);
    }

    /**
     * The class provides the method to compare LogRecord objects by its body size
     */
    public static class BodySizeInBytesComparator implements Comparator<LogRecord>, Serializable {
        /**
         * Compares LogRecord objects
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return 0 if there are equal sizes, -1 if the first one is less than the second one, 1 otherwise
         */
        @Override
        public int compare(LogRecord o1, LogRecord o2) {
            if (o1.bodyBytesSent() == o2.bodyBytesSent()) {
                return 0;
            }
            return (o1.bodyBytesSent() < o2.bodyBytesSent() ? -1 : 1);
        }
    }

    /**
     * The class contains request information
     *
     * @param requestType   GET, POST, etc.
     * @param requestSource the path to source
     * @param requestHTTP   protocol
     */
    public record Request(String requestType, String requestSource, String requestHTTP) {

    }

}
