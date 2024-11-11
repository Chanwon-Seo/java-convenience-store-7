package store.domain;

import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.dto.CartItemDto;
import store.dto.StoreDto;

public class Store {
    private final Map<String, List<Product>> products;
    private final List<Promotion> promotions;
    private Map<String, String> map = new HashMap<>();

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


    public Optional<Product> findByProductNameAndPromotionIsNotNull(String productName) {
        List<Product> productList = findProductsByName(productName);
        return productList.stream()
                .filter(product -> product.getPromotion().isPresent())
                .findFirst();
    }

    public Product findByProductNameAndPromotionIsNotNullOrThrow(String productName) {
        List<Product> productList = findProductsByName(productName);
        return productList.stream()
                .filter(product -> product.getPromotion().isPresent())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }

    public Product findByProductNameAndPromotionIsNull(String productName) {
        return findProductsByName(productName).stream()
                .filter(product -> product.getPromotion().isEmpty())
                .findFirst()
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
        return findProductsByName(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public boolean existsByProductName(String productName) {
        return !products.containsKey(productName);
    }

    public void decreaseStockForOrder(Order order) {
        decreasePromotionProductQuantity(order);
        decreaseNonPromotionProductQuantity(order);
    }

    public void decreasePromotionProductQuantity(Order order) {
        for (OrderItem orderItem : order.getOrderItemsWithPromotion()) {
            Product storeProduct = findByProductNameAndPromotionIsNotNullOrThrow(orderItem.getProduct().getName());
            storeProduct.decreaseQuantity(orderItem.getOrderQuantity());
        }
    }

    public void decreaseNonPromotionProductQuantity(Order order) {
        for (OrderItem orderItem : order.getOrderItemsNonPromotion()) {
            Product nonProduct = findByProductNameAndPromotionIsNull(orderItem.getProduct().getName());
            nonProduct.decreaseQuantity(orderItem.getOrderQuantity());
        }
    }

}