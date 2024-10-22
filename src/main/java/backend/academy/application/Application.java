package backend.academy.application;

import backend.academy.cliparams.CliParams;

public interface Application {
    void run(CliParams cliParams) throws Exception;
}
