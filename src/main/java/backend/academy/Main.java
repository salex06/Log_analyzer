package backend.academy;

import backend.academy.application.Application;
import backend.academy.application.impl.AnalyzerApplication;
import backend.academy.cliparams.CliParams;
import com.beust.jcommander.JCommander;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) throws Exception {
        CliParams cliParams = new CliParams();
        JCommander.newBuilder().addObject(cliParams).build().parse(args);
        Application analyzerApplication = new AnalyzerApplication();
        analyzerApplication.run(cliParams);
    }
}
