package store.dto;

import static store.message.ErrorMessage.INVALID_DATA_FORMAT;

public record ProductDto(
        String name,
        String price,
        String quantity,
        String promotion
) {
    private static final String COMMA_DELIMITER = ",";
    private static final int EXPECTED_DATA_LENGTH = 4;

    public static ProductDto toProductDto(String data) {
        String[] split = data.split(COMMA_DELIMITER, -1);
        validateDataLength(split);
        return new ProductDto(split[0], split[1], split[2], split[3]);
    }

    private static void validateDataLength(String[] split) {
        if (split.length != EXPECTED_DATA_LENGTH) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT.getMessage());
        }
    }
}
