package store.dto;

import static store.message.ErrorMessage.INVALID_DATA_FORMAT;

public record PromotionDto(
        String name,
        String buy,
        String get,
        String startDate,
        String endDate
) {

    public static PromotionDto toPromotionDto(String data) {
        String[] split = data.split(",", -1);
        validateDataLength(split);
        return new PromotionDto(split[0], split[1], split[2], split[3], split[4]);
    }

    private static void validateDataLength(String[] split) {
        if (split.length != 5) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT.getMessage());
        }
    }
}

