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

    public List<Product> findAll() {
        List<Product> flattenedProducts = new ArrayList<>();
        for (List<Product> productList : products.values()) {
            flattenedProducts.addAll(productList);
        }
        return flattenedProducts;
    }

    public boolean existsByProductName(String productName) {
        List<Product> getProducts = products.get(productName);
        return getProducts != null;
    }

    public int findTotalQuantityByProductName(String productName) {
        List<Product> getProducts = products.get(productName);
        int totalQuantity = 0;
        if (getProducts != null) {
            for (Product product : getProducts) {
                totalQuantity += product.getQuantity();
            }
        }
        return totalQuantity;
    }

}