package backend.academy;

import backend.academy.application.Application;
import backend.academy.application.impl.AnalyzerApplication;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) throws Exception {
        Application analyzerApplication = new AnalyzerApplication();
        analyzerApplication.run(args);
    }
}
