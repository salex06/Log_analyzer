package backend.academy.cliparams;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.Getter;

/**
 * The class manages CLI, stores and processes
 * necessary flags and parameters for user input
 */
@Getter
public class CliParams {
    /**
     * The path to log files as a wildcard string
     * (supports URL path or Local file pattern)
     */
    @Parameter(names = "--path", description = "Path to log-files", required = true)
    private String path;

    /**
     * The earliest date for the log
     */
    @Parameter(names = "--from", converter = StringToLocalDateTimeConverter.class,
        description = "The earliest date for log files")
    private LocalDate fromDate;

    /**
     * The latest date for the log
     */
    @Parameter(names = "--to", converter = StringToLocalDateTimeConverter.class,
        description = "The latest date for log files")
    private LocalDate toDate;

    /**
     * The type of output formatting (as markdown or adoc)
     */
    @Parameter(names = "--format", description = "The format of the result (markdown or adoc)",
        validateWith = FileFormatValidator.class)
    private String fileFormat;

    /**
     * The class provides the operation to convert the input from string into LocalDate object
     */
    public static class StringToLocalDateTimeConverter implements IStringConverter<LocalDate> {
        /**
         * Converts data from string into LocalDate object
         *
         * @param s the input string that will be converted
         * @return the LocalDate object - converted string
         * @throws ParameterException if there are parsing errors
         */
        @Override
        public LocalDate convert(String s) {
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                throw new ParameterException("Data " + s + " could not be parsed", e);
            }
        }
    }

    /**
     * The class provides the operation to check the input for correctness
     */
    public static class FileFormatValidator implements IParameterValidator {
        /**
         * Check the input for correctness
         *
         * @param name name of the flag (such as --format)
         * @param val  the checked value passed after the flag
         */
        public void validate(String name, String val) {
            if (!Objects.equals(val, "adoc") && !Objects.equals(val, "markdown")) {
                throw new ParameterException("Parameter " + name + " must be 'adoc' or 'markdown'");
            }
        }
    }
}
