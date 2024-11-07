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
    private static final String NULL_PROMOTION = "null";
    private static final String DEFAULT_QUANTITY = "0";

    public static ProductDto toProductDto(String data) {
        String[] split = data.split(COMMA_DELIMITER, -1);
        validateDataLength(split);
        return new ProductDto(split[0], split[1], split[2], normalizePromotion(split[3]));
    }

    public static ProductDto toGeneralProductDto(ProductDto productDto) {
        return new ProductDto(productDto.name, productDto.price, DEFAULT_QUANTITY, null);
    }

    private static void validateDataLength(String[] split) {
        if (split.length != EXPECTED_DATA_LENGTH) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT.getMessage());
        }
    }

    private static String normalizePromotion(String data) {
        if (NULL_PROMOTION.equals(data)) {
            return null;
        }
        return data;
    }
}
