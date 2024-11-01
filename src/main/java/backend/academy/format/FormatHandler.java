package backend.academy.format;

import backend.academy.format.impl.AsciiDocFormatter;
import backend.academy.log.LogReport;
import java.util.List;
import lombok.Getter;

/**
 * The class keeps an object of a formatting class type and
 * provides method to format the report using the methods
 * of the specified type of formatting
 */
@Getter
public class FormatHandler {
    public static final Formatter DEFAULT_FORMATTER = new AsciiDocFormatter();
    private final Formatter formatter;
    private StringBuilder formattedReport;

    public FormatHandler() {
        this.formatter = DEFAULT_FORMATTER;
    }

    /**
     * Parameterized constructor sets the formatter value to the formatter field
     *
     * @param formatter type of formatting
     */
    public FormatHandler(Formatter formatter) {
        this.formatter = formatter;
    }

    /**
     * The method formats the report using the specified type of formatting
     *
     * @param logReport given log file report
     * @return {@code String} - formatted report
     */
    public String formatReport(LogReport logReport) {
        formattedReport = new StringBuilder();
        appendGeneralInformation(logReport);
        appendSourceInfo(logReport);
        appendResponseCodesInfo(logReport);
        appendRequestsNumberByHourInformation(logReport);
        appendRequestsNumberByRemoteAddress(logReport);
        return formattedReport.toString();
    }

    private void appendRequestsNumberByRemoteAddress(LogReport logReport) {
        List<List<String>> requestsNumberByHour = logReport.getRequestsNumberByRemoteAddressAsTable();
        formattedReport.append(formatter.formatTitle("Наибольшее количество запросов по пользователям"));
        formattedReport.append(formatter.formatTable(requestsNumberByHour));
    }

    private void appendRequestsNumberByHourInformation(LogReport logReport) {
        List<List<String>> requestsNumberByHour = logReport.getRequestsNumberByHourAsTable();
        formattedReport.append(formatter.formatTitle("Количество запросов по часам"));
        formattedReport.append(formatter.formatTable(requestsNumberByHour));
    }

    private void appendResponseCodesInfo(LogReport logReport) {
        List<List<String>> responseCodes = logReport.getResponseCodesAsTable();
        formattedReport.append(formatter.formatTitle("Коды ответа"));
        formattedReport.append(formatter.formatTable(responseCodes));
    }

    private void appendSourceInfo(LogReport logReport) {
        List<List<String>> requestedResources = logReport.getResourcesAsTable();
        formattedReport.append(formatter.formatTitle("Запрашиваемые ресурсы"));
        formattedReport.append(formatter.formatTable(requestedResources));
    }

    private void appendGeneralInformation(LogReport logReport) {
        List<List<String>> generalInfo = logReport.getGeneralInfoAsTable();
        formattedReport.append(formatter.formatTitle("Общая информация"));
        formattedReport.append(formatter.formatTable(generalInfo));
    }
}
