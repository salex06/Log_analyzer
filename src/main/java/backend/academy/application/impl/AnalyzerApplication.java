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
import backend.academy.tools.impl.IOHandlerImpl;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
        ioHandler = new IOHandlerImpl();
        logParser = new LogParser();
    }

    public AnalyzerApplication(IOHandler ioHandler) {
        this();
        this.ioHandler = ioHandler;
    }

    @Override
    public void run(String[] args) throws Exception {
        try {
            CliParams cliParams = new CliParams();

            JCommander.newBuilder().addObject(cliParams).build().parse(args);

            String filePath = cliParams.path();
            Optional<LocalDate> fromDate = Optional.ofNullable(cliParams.fromDate());
            Optional<LocalDate> toDate = Optional.ofNullable(cliParams.toDate());
            Optional<String> fileFormat = Optional.ofNullable(cliParams.fileFormat());
            Optional<String> outputFormat = Optional.ofNullable(cliParams.outputType());
            Optional<String> filterField = Optional.ofNullable(cliParams.fieldName());
            Optional<String> filterValue = Optional.ofNullable(cliParams.fieldValue());

            if (URLPathHandler.URL_PATH_PATTERN.matcher(filePath).matches()) {
                pathHandler = new URLPathHandler();
            } else {
                pathHandler = new LocalPathHandler();
            }

            String defaultFileName;
            if (fileFormat.isPresent() && fileFormat.orElseThrow().equals(String.valueOf("markdown"))) {
                formatter = new FormatHandler(new MarkdownFormatter());
                defaultFileName = MarkdownFormatter.DEFAULT_FILE_NAME;
            } else {
                formatter = new FormatHandler(new AsciiDocFormatter());
                defaultFileName = AsciiDocFormatter.DEFAULT_FILE_NAME;
            }

            if (outputFormat.isPresent() && outputFormat.orElseThrow().equals(String.valueOf("file"))) {
                ioHandler = new IOHandlerImpl(System.in,
                    Files.newOutputStream(Path.of(IOHandlerImpl.PATH_TO_OUTPUT_FILE + defaultFileName)));
            }

            List<Map.Entry<String, Stream<String>>> logsFromPath = pathHandler.handlePath(filePath);

            if (logsFromPath.isEmpty()) {
                ioHandler.write("Лог-файлы не найдены");
                return;
            }

            LogReport logReport =
                logParser.parse(logsFromPath, fromDate.orElse(null), toDate.orElse(null),
                    filterField.orElse(null), filterValue.orElse(null));

            if (logReport == null) {
                ioHandler.write("Не найдены удовлетворяющие фильтрам записи");
            } else {
                ioHandler.write(formatter.formatReport(logReport));
            }
        } catch (IllegalArgumentException e) {
            ioHandler.write("Ошибка: " + e.getMessage() + '\n');
            Logger.log.error("Error: {}", e, e.getCause());
        } catch (ParameterException e) {
            ioHandler.write("Ошибка параметров командной строки: " + e.getMessage() + '\n');
            Logger.log.error("Error entering command line parameters: {}", e, e.getCause());
        }
    }

    @Slf4j
    private static class Logger {

    }
}
