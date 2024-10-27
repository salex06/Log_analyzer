package backend.academy.format;

import backend.academy.log.LogReport;
import java.util.List;

/**
 * The class keeps an object of a formatting class type and
 * provides method to format the report using the methods
 * of the specified type of formatting
 */
public class FormatHandler {
    private final Formatter formatter;
    private StringBuilder formattedReport;

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
     * @param logReport given log file report
     * @return {@code String} - formatted report
     */
    public String formatReport(LogReport logReport) {
        formattedReport = new StringBuilder();
        appendGeneralInformation(logReport);
        appendSourceInfo(logReport);
        appendResponseCodesInfo(logReport);
        return formattedReport.toString();
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
