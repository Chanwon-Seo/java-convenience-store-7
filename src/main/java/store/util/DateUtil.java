package store.util;

import static store.message.ErrorMessage.FILE_READ_ERROR;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String MIDNIGHT_TIME_FORMAT = "T00:00:00";

    public static LocalDateTime dateParse(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        try {
            return LocalDateTime.parse(date + MIDNIGHT_TIME_FORMAT, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(FILE_READ_ERROR.getMessage());
        }
    }

}