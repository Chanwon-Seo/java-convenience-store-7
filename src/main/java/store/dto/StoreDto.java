package store.dto;

import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;

public record StoreDto(
        Map<String, List<Product>> products,
        List<Promotion> promotions
) {
    public static StoreDto of(Map<String, List<Product>> products, List<Promotion> promotions) {
        return new StoreDto(products, promotions);
    }
}