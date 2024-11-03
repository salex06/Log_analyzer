package backend.academy.cliparams;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

/**
 * The class manages CLI, stores and processes
 * necessary flags and parameters for user input
 */
@Getter
@Parameters(parametersValidators = CliParams.ExtraFilteringValidator.class)
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

    @Parameter(names = "--filter-field", description = "Additional filtering by a given field")
    String fieldName;

    @Parameter(names = "--filter-value", description = "Value for filtering", converter = GlobToRegexConverter.class)
    String fieldValue;

    /**
     * The type of output (to the console or to the file)
     */
    @Parameter(names = "--output", description = "Output type (to the console or to the file)",
        validateWith = OutputTypeValidator.class)
    private String outputType;

    /**
     * The class provides the operation to check the input for correctness
     */
    public static class OutputTypeValidator implements IParameterValidator {
        /**
         * Check the output type for correctness
         *
         * @param name name of the flag (such as --output)
         * @param val  the checked value passed after the flag
         */
        @Override
        public void validate(String name, String val) {
            if (!Objects.equals(val, "file") && !Objects.equals(val, "console")) {
                throw new ParameterException(name + " must be 'file' or 'console'");
            }
        }
    }

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

    /**
     * The class provides the operation to check matching parameters --filter-field and --filter-value
     */
    public static class ExtraFilteringValidator implements IParametersValidator {
        /**
         * Check matching parameters --filter-field and --filter-value
         * (both parameters are not set or both are set)
         *
         * @param map dictionary of parameters
         */
        @Override
        public void validate(Map<String, Object> map) throws ParameterException {
            String fieldFlagName = "--filter-field";
            String valueFlagName = "--filter-value";
            if (map.get(fieldFlagName) != null && map.get(valueFlagName) == null) {
                throw new ParameterException(
                    "--filter-value was expected"
                );
            }
            if (map.get(fieldFlagName) == null && map.get(valueFlagName) != null) {
                throw new ParameterException(
                    "--filter-field was expected"
                );
            }
        }
    }

    /**
     * The class provides operation to convert glob string to regex
     */
    public static class GlobToRegexConverter implements IStringConverter<String> {
        /**
         * Convert glob to regex
         *
         * @param glob command line argument
         * @return regular expression in the form of the string
         */
        @Override
        public String convert(String glob) {
            StringBuilder regex = new StringBuilder();
            regex.append('^');
            for (int i = 0; i < glob.length(); i++) {
                char c = glob.charAt(i);
                switch (c) {
                    case '*' -> regex.append(".*");
                    case '?' -> regex.append('.');
                    case '.' -> regex.append("\\.");
                    case '\\' -> regex.append("\\\\");
                    default -> regex.append(c);
                }
            }
            regex.append('$');
            return regex.toString();
        }
    }
}
