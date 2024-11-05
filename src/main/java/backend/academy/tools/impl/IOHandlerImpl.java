package backend.academy.tools.impl;

import backend.academy.tools.IOHandler;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lombok.Getter;

/**
 * ConsoleHandler allows to interact with the console
 * (reading/writing data).
 * It is based on the Scanner and PrintStream classes using
 * System.in and System.out or other I/O streams which is provided
 * by the parameterized constructor.
 */
@Getter
public class IOHandlerImpl implements IOHandler {
    /**
     * A field containing a Scanner type object - input stream
     */
    private final Scanner inputStream;
    /**
     * A field containing a PrintStream type object - output stream
     */
    private final PrintStream outputStream;

    /**
     * Default constructor which sets a standard System.in and System.out streams
     */
    public IOHandlerImpl() {
        inputStream = new Scanner(System.in, StandardCharsets.UTF_8);
        outputStream = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    /**
     * Parameterized constructor which sets the passed parameters as input and output streams
     * @param inputStream data reading stream
     * @param outputStream data writing stream
     */
    public IOHandlerImpl(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = new Scanner(inputStream, StandardCharsets.UTF_8);
        this.outputStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);
    }

    public void write(String data) {
        outputStream.print(data);
    }

    public String read() {
        return inputStream.nextLine();
    }

}

