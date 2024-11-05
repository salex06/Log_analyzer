package backend.academy.tools.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IOHandlerImplTest {
    IOHandlerImpl IOHandlerImpl;

    @Test
    @DisplayName("Ensure the fields are initialized successfully")
    void ensureConstructorsWorksSuccessfully() throws FileNotFoundException {
        IOHandlerImpl = new IOHandlerImpl();
        assertThat(IOHandlerImpl.inputStream()).isInstanceOf(Scanner.class);
        assertThat(IOHandlerImpl.outputStream()).isInstanceOf(PrintStream.class);

        IOHandlerImpl = new IOHandlerImpl(System.in, System.out);
        assertThat(IOHandlerImpl.inputStream()).isInstanceOf(Scanner.class);
        assertThat(IOHandlerImpl.outputStream()).isInstanceOf(PrintStream.class);

        IOHandlerImpl =
            new IOHandlerImpl(System.in, new FileOutputStream("src/test/java/backend/academy/tools/impl/testFile.txt"));
        assertThat(IOHandlerImpl.inputStream()).isInstanceOf(Scanner.class);
        assertThat(IOHandlerImpl.outputStream()).isInstanceOf(PrintStream.class);
    }

    @Test
    @DisplayName("Ensure the read method works correctly")
    void ensureReadWorksCorrectly() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("data from input".getBytes());
        IOHandlerImpl = new IOHandlerImpl(byteArrayInputStream, System.out);

        assertEquals("data from input", IOHandlerImpl.read());
    }

    @Test
    @DisplayName("Ensure the writeMessage method works correctly")
    void ensureWriteMessageWorksCorrectly() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOHandlerImpl = new IOHandlerImpl(System.in, byteArrayOutputStream);

        IOHandlerImpl.write("data to output");

        assertEquals("data to output", byteArrayOutputStream.toString());
    }

    @Test
    @DisplayName("Ensure the writeMessage method works correctly for files")
    void ensureWriteMessageWorksCorrectlyForFiles() throws FileNotFoundException {
        String expectedMessage = "data to output";
        IOHandlerImpl =
            new IOHandlerImpl(System.in, new FileOutputStream("src/test/java/backend/academy/tools/impl/testFile.txt"));

        IOHandlerImpl.write(expectedMessage);

        Scanner fileReader = new Scanner(new FileInputStream("src/test/java/backend/academy/tools/impl/testFile.txt"));
        assertEquals(expectedMessage, fileReader.nextLine());
    }
}
