package backend.academy.filter;

import backend.academy.log.LogRecord;
import java.time.LocalDate;

/**
 * Filter interface provides operations to filter the LogRecord
 * objects by date and filter fields
 */
public interface Filter {
    /**
     * Check the logRecord compliance with the filter parameters
     *
     * @param logRecord   record to be checked
     * @param from        starting date
     * @param to          end date
     * @param filterField the field for which additional filtering is performed
     * @param filterValue the value of the field to filter
     * @return true if record passes check
     */
    boolean filter(LogRecord logRecord, LocalDate from, LocalDate to, String filterField, String filterValue);
}
