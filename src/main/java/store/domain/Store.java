package store.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.dto.StoreDto;

public class Store {
    private Map<String, List<Product>> products;
    private List<Promotion> promotions;

    public Store(StoreDto storeDto) {
        this.products = storeDto.products();
        this.promotions = storeDto.promotions();
    }

    public List<Product> flattenProducts() {
        List<Product> flattenedProducts = new ArrayList<>();
        for (List<Product> productList : products.values()) {
            flattenedProducts.addAll(productList);
        }
        return flattenedProducts;
    }

    public Map<String, List<Product>> getProducts() {
        return products;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }
}