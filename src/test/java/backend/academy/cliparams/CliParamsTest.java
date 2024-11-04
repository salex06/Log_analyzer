package backend.academy.cliparams;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CliParamsTest {
    CliParams cliParams;

    @BeforeEach
    void setup() {
        cliParams = new CliParams();
    }

    @Test
    @DisplayName("Ensure that absence of the --path flag causes Exception throwing")
    void ensureAbsencePathCausesException() {
        String[] params = new String[] {"--from", "2024-06-04", "--to", "2025-10-12", "--format", "markdown"};
        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            params));
    }

    @Test
    @DisplayName("Ensure that absence of the --from and --to flags does nothing")
    void ensureAbsenceFromAndToIsUnimportant() {
        String[] params = {"--path", "somePath/AndSomeDirs/AndFile.txt", "--format", "markdown"};

        JCommander.newBuilder().addObject(cliParams).build().parse(params);

        assertNull(cliParams.fromDate());
        assertNull(cliParams.toDate());
    }

    @ParameterizedTest
    @CsvSource(value = {"2015-12-13, 2015, 12, 13", "2010-10-25, 2010, 10, 25", "2022-02-09, 2022, 2, 9"})
    @DisplayName("Ensure correct date converts successfully")
    void ensureCorrectDateConvertsSuccessfully(String date, int expectedYear, int expectedMonth, int expectedDay) {
        String[] params = {"--path", "somePath/someFile.txt", "--from", date};

        JCommander.newBuilder().addObject(cliParams).build().parse(params);

        assertEquals(expectedYear, cliParams.fromDate().getYear());
        assertEquals(expectedMonth, cliParams.fromDate().getMonth().getValue());
        assertEquals(expectedDay, cliParams.fromDate().getDayOfMonth());

        params = new String[] {"--path", "somePath/someFile.txt", "--to", date};
        JCommander.newBuilder().addObject(cliParams).build().parse(params);

        assertEquals(expectedYear, cliParams.toDate().getYear());
        assertEquals(expectedMonth, cliParams.toDate().getMonth().getValue());
        assertEquals(expectedDay, cliParams.toDate().getDayOfMonth());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10-10-2020", "2010-10asb-25", "20225-02-09", "31-12-2023", "11-11-11", "1234-1234-123"})
    @DisplayName("Ensure wrong date conversion throws an Exception")
    void ensureWrongDateConversionThrowsException(String date) {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--from", date};
        String[] finalParams = params;

        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            finalParams));

        params = new String[] {"--path", "somePath/someFile.txt", "--to", date};
        String[] finalParams1 = params;

        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            finalParams1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"adoc", "markdown"})
    @DisplayName("Ensure the format is set correctly")
    void ensureFormatIsSetCorrectly(String format) {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--format", format};

        JCommander.newBuilder().addObject(cliParams).build().parse(
            params);

        assertEquals(format, cliParams.fileFormat());
    }

    @ParameterizedTest
    @ValueSource(strings = {"sadf", "123", "o", "markdow", "doc"})
    @DisplayName("Ensure the format is not set correctly")
    void ensureFormatIsNotSetCorrectly(String format) {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--format", format};

        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"file", "console"})
    @DisplayName("Ensure the output type is initialized correctly")
    void ensureOutputTypeIsInitializedCorrectly(String expected) {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--output", expected};

        JCommander.newBuilder().addObject(cliParams).build().parse(
            params);

        assertEquals(expected, cliParams.outputType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"sdafas", " ", "-1", "0"})
    @DisplayName("Ensure the wrong output type throws exception")
    void ensureWrongOutputTypeThrowsException(String current) {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--output", current};

        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            params));
    }

    @Test
    @DisplayName("Ensure --filter-field parameter without --filter-value throws exception")
    void ensureFilterFieldWithoutFilterValueThrowsException() {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--filter-field", "some field"};

        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            params));
    }

    @Test
    @DisplayName("Ensure --filter-value parameter without --filter-field throws exception")
    void ensureFilterValueWithoutFilterFieldThrowsException() {
        String[] params = new String[] {"--path", "somePath/someFile.txt", "--filter-value", "some value"};

        assertThrows(ParameterException.class, () -> JCommander.newBuilder().addObject(cliParams).build().parse(
            params));
    }

    @Test
    @DisplayName("Ensure --filter-value with --filter-field is initialized correctly")
    void ensureFilterValueAndFilterFieldDoNotThrowException() {
        String field = "agent";
        String value = "test";
        String[] params =
            new String[] {"--path", "somePath/someFile.txt", "--filter-field", field, "--filter-value",
                value};
        String expectedValue = "^test$";

        JCommander.newBuilder().addObject(cliParams).build().parse(
            params);

        assertEquals(field, cliParams.fieldName);
        assertEquals(expectedValue, cliParams.fieldValue);
    }

    @Test
    @DisplayName("Ensure absence of a parameter value causes an exception")
    void ensureAbsenceOfParameterValueCausesException() {
        String[] params =
            new String[] {"--path", "--filter-field", "some_field"};

        assertThrows(ParameterException.class,
            () -> JCommander.newBuilder().addObject(cliParams).build().parse(params));
    }
}
