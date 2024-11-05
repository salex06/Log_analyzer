package backend.academy.application.impl;

import backend.academy.format.impl.AsciiDocFormatter;
import backend.academy.format.impl.MarkdownFormatter;
import backend.academy.path.impl.LocalPathHandler;
import backend.academy.path.impl.URLPathHandler;
import backend.academy.tools.IOHandler;
import backend.academy.tools.impl.ConsoleIOHandler;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AnalyzerApplicationTest {
    AnalyzerApplication analyzerApplication = new AnalyzerApplication();

    @Test
    @DisplayName("Ensure all the fields are initialized correctly (1)")
    void ensureAllComponentsAreInitializedCorrectly1() throws Exception {
        String[] params = {"--path", "logs/**/*.txt", "--format", "markdown"};

        analyzerApplication.run(params);

        assertThat(analyzerApplication.formatter().formatter()).isInstanceOf(MarkdownFormatter.class);
        assertThat(analyzerApplication.pathHandler()).isInstanceOf(LocalPathHandler.class);
    }

    @Test
    @DisplayName("Ensure all the fields are initialized correctly (2)")
    void ensureAllComponentsAreInitializedCorrectly2() throws Exception {
        String[] params =
            {"--path",
                "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
                "--format",
                "adoc"};

        analyzerApplication.run(params);

        assertThat(analyzerApplication.formatter().formatter()).isInstanceOf(AsciiDocFormatter.class);
        assertThat(analyzerApplication.pathHandler()).isInstanceOf(URLPathHandler.class);
    }

    @Test
    @DisplayName("Ensure all the fields are initialized correctly (3)")
    void ensureAllComponentsAreInitializedCorrectly3() throws Exception {
        String[] params = {"--path", "logs/**/*.txt"};

        analyzerApplication.run(params);

        assertThat(analyzerApplication.formatter().formatter()).isInstanceOf(AsciiDocFormatter.class);
        assertThat(analyzerApplication.pathHandler()).isInstanceOf(LocalPathHandler.class);
    }

    @Test
    @DisplayName("Ensure the run method will display a message about the absence of logs")
    void ensureRunMethodDisplayMessageAboutAbsenceOfLogs() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOHandler ioHandler = new ConsoleIOHandler(System.in, byteArrayOutputStream);
        analyzerApplication = new AnalyzerApplication(ioHandler);
        String[] params = {"--path", "wrong/**/*.txt"};

        analyzerApplication.run(params);

        assertThat(byteArrayOutputStream.toString()).isEqualTo("Лог-файлы не найдены");
    }

    @Test
    @DisplayName("Ensure the run method will display a message about the absence of suitable logs (after filtering)")
    void ensureRunMethodDisplayMessageAboutAbsenceOfSuitableLogs() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOHandler ioHandler = new ConsoleIOHandler(System.in, byteArrayOutputStream);
        analyzerApplication = new AnalyzerApplication(ioHandler);
        String[] params = {"--path", "logs/subLogs_1/30_12_20.txt", "--from", "2020-12-31"};

        analyzerApplication.run(params);

        assertThat(byteArrayOutputStream.toString()).isEqualTo("Не найдены удовлетворяющие фильтрам записи");
    }
}
