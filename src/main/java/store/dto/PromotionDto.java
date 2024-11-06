package store.dto;

import static store.message.ErrorMessage.INVALID_DATA_FORMAT;

public record PromotionDto(
        String name,
        String buy,
        String get,
        String startDate,
        String endDate
) {
    private static final String COMMA_DELIMITER = ",";
    private static final int EXPECTED_DATA_LENGTH = 5;

    public static PromotionDto toPromotionDto(String data) {
        String[] split = data.split(COMMA_DELIMITER, -1);
        validateDataLength(split);
        return new PromotionDto(split[0], split[1], split[2], split[3], split[4]);
    }

    private static void validateDataLength(String[] split) {
        if (split.length != EXPECTED_DATA_LENGTH) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT.getMessage());
        }
    }
}

