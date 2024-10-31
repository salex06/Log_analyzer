package backend.academy.application.impl;

import backend.academy.cliparams.CliParams;
import backend.academy.format.impl.AsciiDocFormatter;
import backend.academy.format.impl.MarkdownFormatter;
import backend.academy.path.impl.LocalPathHandler;
import backend.academy.path.impl.URLPathHandler;
import com.beust.jcommander.JCommander;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AnalyzerApplicationTest {
    AnalyzerApplication analyzerApplication = new AnalyzerApplication();

    @Test
    @DisplayName("Ensure all the fields are initialized correctly (1)")
    void ensureAllComponentsAreInitializedCorrectly1() throws Exception {
        CliParams cliParams = new CliParams();
        String[] params = {"--path", "logs/**/*.txt", "--format", "markdown"};
        JCommander.newBuilder().addObject(cliParams).build().parse(params);

        analyzerApplication.run(cliParams);

        assertThat(analyzerApplication.formatter().formatter()).isInstanceOf(MarkdownFormatter.class);
        assertThat(analyzerApplication.pathHandler()).isInstanceOf(LocalPathHandler.class);
    }

    @Test
    @DisplayName("Ensure all the fields are initialized correctly (2)")
    void ensureAllComponentsAreInitializedCorrectly2() throws Exception {
        CliParams cliParams = new CliParams();
        String[] params =
            {"--path", "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs",
                "--format", "adoc"};
        JCommander.newBuilder().addObject(cliParams).build().parse(params);

        analyzerApplication.run(cliParams);

        assertThat(analyzerApplication.formatter().formatter()).isInstanceOf(AsciiDocFormatter.class);
        assertThat(analyzerApplication.pathHandler()).isInstanceOf(URLPathHandler.class);
    }

    @Test
    @DisplayName("Ensure all the fields are initialized correctly (3)")
    void ensureAllComponentsAreInitializedCorrectly3() throws Exception {
        CliParams cliParams = new CliParams();
        String[] params = {"--path", "logs/**/*.txt"};
        JCommander.newBuilder().addObject(cliParams).build().parse(params);

        analyzerApplication.run(cliParams);

        assertThat(analyzerApplication.formatter().formatter()).isInstanceOf(AsciiDocFormatter.class);
        assertThat(analyzerApplication.pathHandler()).isInstanceOf(LocalPathHandler.class);
    }
}
