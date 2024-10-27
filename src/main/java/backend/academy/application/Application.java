package backend.academy.application;

import backend.academy.cliparams.CliParams;

/**
 * The interface allows to run the application
 */
public interface Application {
    /**
     * The method starts the application
     *
     * @param cliParams command line options (flags and its values)
     * @throws Exception if errors occur while the program is running
     */
    void run(CliParams cliParams) throws Exception;
}
