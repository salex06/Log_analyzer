package backend.academy.application.impl;

import backend.academy.application.Application;
import backend.academy.cliparams.CliParams;
import backend.academy.format.FormatHandler;
import backend.academy.format.impl.AsciiDocFormatter;
import backend.academy.format.impl.MarkdownFormatter;
import backend.academy.log.LogReport;
import backend.academy.parser.Parser;
import backend.academy.parser.impl.LogParser;
import backend.academy.path.PathHandler;
import backend.academy.path.impl.LocalPathHandler;
import backend.academy.path.impl.URLPathHandler;
import backend.academy.tools.IOHandler;
import backend.academy.tools.impl.ConsoleIOHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * The class represents an application which analyze the log files
 * and collect statistics about them
 */
@Getter
public class AnalyzerApplication implements Application {
    private PathHandler pathHandler;
    private Parser logParser;
    private IOHandler ioHandler;
    private FormatHandler formatter;

    public AnalyzerApplication() {
        ioHandler = new ConsoleIOHandler();
        logParser = new LogParser();
    }

    @Override
    public void run(CliParams cliParams) throws Exception {
        String filePath = cliParams.path();
        Optional<LocalDate> fromDate = Optional.ofNullable(cliParams.fromDate());
        Optional<LocalDate> toDate = Optional.ofNullable(cliParams.toDate());
        Optional<String> fileFormat = Optional.ofNullable(cliParams.fileFormat());

        if (filePath.startsWith("http")) {
            pathHandler = new URLPathHandler();
        } else {
            pathHandler = new LocalPathHandler();
        }

        if (fileFormat.isPresent() && fileFormat.orElseThrow().equals(String.valueOf("markdown"))) {
            formatter = new FormatHandler(new MarkdownFormatter());
        } else {
            formatter = new FormatHandler(new AsciiDocFormatter());
        }

        List<Map.Entry<String, Stream<String>>> logsFromPath = pathHandler.handlePath(filePath);

        LogReport logReport = logParser.parse(logsFromPath, fromDate.orElse(null), toDate.orElse(null));

        ioHandler.write(formatter.formatReport(logReport));
    }
}
