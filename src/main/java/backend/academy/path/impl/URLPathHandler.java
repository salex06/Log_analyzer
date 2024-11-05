package backend.academy.path.impl;

import backend.academy.path.PathHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * The class contains method to handle the path in the network
 * and find log files using a URL
 */
public class URLPathHandler implements PathHandler {
    private static final int OK = 200;

    @Override
    public List<Map.Entry<String, Stream<String>>> handlePath(String path) throws Exception {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(path))
                .header("Content-type", "application/json")
                .GET()
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != OK) {
                return List.of();
            }
            return List.of(Map.entry(path, response.body().lines()));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new Exception(e);
        }
    }

    public static final Pattern urlPathPattern =
        Pattern.compile("^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}"
            + "\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$");
}
