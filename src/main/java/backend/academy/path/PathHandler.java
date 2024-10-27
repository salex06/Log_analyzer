package backend.academy.path;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The interface provides operation to handle the path
 * specified as a string and get the contents of the log files located on the path
 */
public interface PathHandler {
    /**
     * Process the path specified as a string and gets the log file
     *
     * @param path the path to the file (on the network or in the local file system)
     * @return the list of entries - the name of the log file
     *     and its contents in the form of a stream of strings
     * @throws Exception if an error occurs while trying to access or read a file
     */
    List<Map.Entry<String, Stream<String>>> handlePath(String path) throws Exception;
}
