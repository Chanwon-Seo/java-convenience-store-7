package store.domain;

import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.dto.CartItemDto;
import store.dto.StoreDto;

public class Store {
    private final Map<String, List<Product>> products;
    private final List<Promotion> promotions;

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

    public Product findProductsByProductNameAndPromotion(String productName) {
        List<Product> productList = products.get(productName);
        return productList.stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }

    public List<Product> findProductsByName(String productName) {
        return products.get(productName);
    }

    public boolean isTotalQuantityByProductName(CartItemDto cartItemDto) {
        if (existsByProductName(cartItemDto.productName())) {
            return false;
        }
        return cartItemDto.quantity() <= findTotalQuantityByProductName(cartItemDto.productName());
    }

    public int findTotalQuantityByProductName(String productName) {
        if (existsByProductName(productName)) {
            return 0;
        }
        return products.get(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public boolean existsByProductName(String productName) {
        return !products.containsKey(productName);
    }

}