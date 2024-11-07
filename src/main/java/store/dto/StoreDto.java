package store.dto;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public record StoreDto(
        List<Product> products,
        List<Promotion> promotions
) {
    public static StoreDto of(List<Product> products, List<Promotion> promotions) {
        return new StoreDto(products, promotions);
    }
}