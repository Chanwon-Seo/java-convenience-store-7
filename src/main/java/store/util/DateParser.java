package store.util;

import static store.constants.ParserConstants.DATE_TIME_FORMAT;
import static store.constants.ParserConstants.MIDNIGHT_TIME_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateParser {

    public static LocalDateTime dateParse(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(date + MIDNIGHT_TIME_FORMAT, formatter);
    }

}