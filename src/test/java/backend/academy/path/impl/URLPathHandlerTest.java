package backend.academy.path.impl;

import backend.academy.path.PathHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class URLPathHandlerTest {
    private final PathHandler pathHandler = new URLPathHandler();
    private final String path =
        "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";

    @Test
    @DisplayName("Ensure handlePath returns correct filename")
    void ensureHandlePathReturnsCorrectFileName() throws Exception {

        String actual = pathHandler.handlePath(path).getFirst().getKey();

        assertEquals(path, actual);
    }

    @Test
    @DisplayName("Ensure handlePath returns correct file content")
    void ensureHandlePathReturnsCorrectFileContent() throws Exception {
        String expected =
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        String actual = pathHandler.handlePath(path).getFirst().getValue().toList().getFirst();

        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Ensure handlePath throws an Exception if the path is incorrect")
    void ensureMethodThrowsExceptionIfPathIncorrect(){
        String path = "it is not url";

        assertThrows(Exception.class, () -> pathHandler.handlePath(path));
    }
}
