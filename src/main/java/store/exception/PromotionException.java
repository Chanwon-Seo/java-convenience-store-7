package store.exception;

import static store.message.ErrorMessage.EMPTY_DATE;
import static store.message.ErrorMessage.INVALID_DATE_FORMAT;
import static store.message.ErrorMessage.NON_NUMERIC;
import static store.message.ErrorMessage.START_DATE_AFTER_END_DATE;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
import store.dto.PromotionDto;
import store.util.DateParser;

public abstract class PromotionException {
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile(DATE_PATTERN);

    public static void validate(PromotionDto promotionDto) {
        validateEmptyField(promotionDto.name());
        validateNumericValue(promotionDto.buy());
        validateNumericValue(promotionDto.get());
        validateDateFormat(promotionDto.startDate());
        validateDateFormat(promotionDto.endDate());
        validateDateOrder(promotionDto.startDate(), promotionDto.endDate());
    }

    public static void validateEmptyField(String date) {
        if (date.isBlank()) {
            throw new IllegalArgumentException(EMPTY_DATE.getMessage());
        }
    }

    public static void validateNumericValue(String data) {
        validateEmptyField(data);
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isDigit(data.charAt(i))) {
                throw new IllegalArgumentException(NON_NUMERIC.getMessage());
            }
        }
    }

    public static void validateDateFormat(String date) {
        if (!DATE_FORMAT_PATTERN.matcher(date).matches()) {
            throw new IllegalArgumentException(INVALID_DATE_FORMAT.getMessage());
        }
    }

    public static void validateDateOrder(String startDate, String endDate) {
        LocalDateTime parsedStartDate = DateParser.dateParse(startDate);
        LocalDateTime parsedEndDate = DateParser.dateParse(endDate);
        if (parsedStartDate.isAfter(parsedEndDate)) {
            throw new IllegalArgumentException(START_DATE_AFTER_END_DATE.getMessage());
        }
    }
}
