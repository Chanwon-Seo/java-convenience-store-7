package store.domain;

import static store.constants.PromotionConstants.NO_PROMOTION_SUFFIX;
import static store.constants.PromotionConstants.PROMOTION_SUFFIX;
import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import store.dto.CartItemDto;
import store.dto.StoreDto;

public class Store {
    private final Map<String, Product> products;

    public Store(StoreDto storeDto) {
        this.products = storeDto.products();
    }

    public List<Product> findAll() {
        return products.values().stream().toList();
    }

    public Product findByProductNameAndPromotionIsNotNullOrThrow(String productName) {
        return Optional.ofNullable(products.get(productName + PROMOTION_SUFFIX))
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }

    public Product findByProductNameAndPromotionIsNotNull(String productName) {
        return products.get(createPromotionKey(productName));
    }

    public Product findByProductNameAndPromotionIsNull(String productName) {
        return products.get(createNoPromotionKey(productName));
    }

    public boolean isTotalQuantityByProductName(CartItemDto cartItemDto) {
        if (!existsByProductName(cartItemDto.productName())) {
            return false;
        }
        return cartItemDto.quantity() <= findTotalQuantityByProductName(cartItemDto.productName());
    }

    public int findTotalQuantityByProductName(String productName) {
        if (!hasPromotionAndNoPromotion(productName)) {
            return 0;
        }

        return findByProductName(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public List<Product> findByProductName(String productName) {
        return Stream.of(createNoPromotionKey(productName), createPromotionKey(productName)).map(products::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public boolean hasPromotionAndNoPromotion(String productName) {
        return products.containsKey(createNoPromotionKey(productName)) &&
                products.containsKey(createPromotionKey(productName));
    }

    public boolean existsByProductName(String productName) {
        return products.containsKey(createNoPromotionKey(productName)) && products.containsKey(
                createPromotionKey(productName));
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

    public String createNoPromotionKey(String productName) {
        return productName + NO_PROMOTION_SUFFIX;
    }

    public String createPromotionKey(String productName) {
        return productName + PROMOTION_SUFFIX;
    }

}