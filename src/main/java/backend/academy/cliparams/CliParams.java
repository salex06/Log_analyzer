package backend.academy.cliparams;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.Getter;

@Getter
public class CliParams {
    @Parameter(names = "--path", description = "Path to log-files", required = true)
    private String path;

    @Parameter(names = "--from", converter = StringToLocalDateTimeConverter.class,
        description = "The earliest date for log files")
    private LocalDate fromDate;

    @Parameter(names = "--to", converter = StringToLocalDateTimeConverter.class,
        description = "The latest date for log files")
    private LocalDate toDate;

    @Parameter(names = "--format", description = "The format of the result (markdown or adoc)",
        validateWith = FileFormatValidator.class)
    private String fileFormat;

    public static class StringToLocalDateTimeConverter implements IStringConverter<LocalDate> {
        @Override
        public LocalDate convert(String s) {
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                throw new ParameterException("Data '2015-06-043' could not be parsed", e);
            }
        }
    }

    public static class FileFormatValidator implements IParameterValidator {
        public void validate(String name, String val) {
            if (!Objects.equals(val, "adoc") && !Objects.equals(val, "markdown")) {
                throw new ParameterException("Parameter " + name + " must be 'adoc' or 'markdown'");
            }
        }
    }
}
