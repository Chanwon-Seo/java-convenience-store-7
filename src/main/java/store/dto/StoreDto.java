package store.dto;

import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;

public record StoreDto(
        Map<String, Product> products,
        List<Promotion> promotions
) {
}