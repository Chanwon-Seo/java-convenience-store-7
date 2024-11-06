package store.dto;

import java.util.List;

public record StoreInitializationDto(
        List<String> products,
        List<String> promotions
) {
    public static StoreInitializationDto of(List<String> products, List<String> promotions) {
        return new StoreInitializationDto(products, promotions);
    }

}
