package backend.academy.format;

import backend.academy.log.LogReport;
import java.util.List;

public class FormatHandler {
    private final Formatter formatter;
    private StringBuilder formattedReport;

    public FormatHandler(Formatter formatter) {
        this.formatter = formatter;
    }

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
