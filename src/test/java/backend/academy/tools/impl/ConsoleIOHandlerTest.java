package backend.academy.tools.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleIOHandlerTest {
    ConsoleIOHandler consoleIOHandler;

    @Test
    @DisplayName("Ensure the fields are initialized successfully")
    void ensureConstructorsWorksSuccessfully() {
        consoleIOHandler = new ConsoleIOHandler();
        assertThat(consoleIOHandler.inputStream()).isInstanceOf(Scanner.class);
        assertThat(consoleIOHandler.outputStream()).isInstanceOf(PrintStream.class);

        consoleIOHandler = new ConsoleIOHandler(System.in, System.out);
        assertThat(consoleIOHandler.inputStream()).isInstanceOf(Scanner.class);
        assertThat(consoleIOHandler.outputStream()).isInstanceOf(PrintStream.class);
    }

    @Test
    @DisplayName("ensure the read method works correctly")
    void ensureReadWorksCorrectly() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("data from input".getBytes());
        consoleIOHandler = new ConsoleIOHandler(byteArrayInputStream, System.out);

        assertEquals("data from input", consoleIOHandler.read());
    }

    @Test
    @DisplayName("ensure the writeMessage method works correctly")
    void ensureWriteMessageWorksCorrectly() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        consoleIOHandler = new ConsoleIOHandler(System.in, byteArrayOutputStream);

        consoleIOHandler.write("data to output");

        assertEquals("data to output", byteArrayOutputStream.toString());
    }
}
