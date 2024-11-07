package store.dto;

public record PromotionDto(
        String name,
        String buy,
        String get,
        String startDate,
        String endDate
) {
    public static PromotionDto toPromotionDto(String name, String buy, String get, String startDate, String endDate) {
        return new PromotionDto(name, buy, get, startDate, endDate);
    }
}

