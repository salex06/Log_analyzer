package backend.academy.application;

/**
 * The interface allows to run the application
 */
public interface Application {
    /**
     * The method starts the application
     *
     * @param args command line options (flags and its values)
     * @throws Exception if errors occur while the program is running
     */
    void run(String[] args) throws Exception;
}
