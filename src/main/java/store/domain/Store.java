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
    private final Map<String, Product> products; //"콜라_PROMO", {Product} , "콜라_NO_PROMO",{Product}

    public Store(StoreDto storeDto) {
        this.products = storeDto.products();
    }

    public List<Product> findAll() {
        return products.values().stream().toList();
    }

    /**
     * 특정 프로모션 상품을 조회합니다. 조회된 상품이 없는 경우 예외가 발생합니다.
     */
    public Product findByProductNameAndPromotionIsNotNullOrThrow(String productName) {
        return Optional.ofNullable(products.get(createPromotionKey(productName)))
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }

    /**
     * 특정 상품을 조회 후 반환합니다.
     */
    public Product findByProductNameAndPromotionIsNotNull(String productName) {
        return products.get(createPromotionKey(productName));
    }

    /**
     * 일반 상품을 조회 후 반환합니다.
     */
    public Product findByProductNameAndPromotionIsNull(String productName) {
        return products.get(createNoPromotionKey(productName));
    }

    /**
     * 장바구니에 담긴 제품의 수량이 재고 수량을 초과하지 않는지 확인
     */
    public boolean isTotalQuantityByProductName(CartItemDto cartItemDto) {
        if (!existsByProductName(cartItemDto.productName())) {
            return false;
        }
        return cartItemDto.quantity() <= findTotalQuantityByProductName(cartItemDto.productName());
    }

    /**
     * 제품 이름으로 재고 수량을 합산하여 총 수량을 반환
     */
    public int findTotalQuantityByProductName(String productName) {
        if (!existsByProductName(productName)) {
            return 0;
        }
        return findByProductName(productName).stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

    /**
     * 특정 상품명을 모두 조회합니다.
     */
    public List<Product> findByProductName(String productName) {
        return Stream.of(createPromotionKey(productName), createNoPromotionKey(productName))
                .map(products::get)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 프로모션 및 일반 상품이 등록된 상품인지 검증합니다.
     */
    public boolean existsByProductName(String productName) {
        return products.containsKey(createNoPromotionKey(productName)) ||
                products.containsKey(createPromotionKey(productName));
    }

    /**
     * 주문한 프로모션 상품 및 일반 상품 수량만큼 차감합니다.
     */
    public void decreaseStockForOrder(Order order) {
        decreasePromotionProductQuantity(order);
        decreaseNonPromotionProductQuantity(order);
    }

    /**
     * 프로모션이 있는 제품의 재고를 차감
     */
    public void decreasePromotionProductQuantity(Order order) {
        for (OrderItem orderItem : order.getOrderItemsWithPromotion()) {
            Product storeProduct = findByProductNameAndPromotionIsNotNullOrThrow(orderItem.getProduct().getName());
            storeProduct.decreaseQuantity(orderItem.getOrderQuantity());
        }
    }

    /**
     * 일반 상품의 제품의 재고를 차감
     */
    public void decreaseNonPromotionProductQuantity(Order order) {
        for (OrderItem orderItem : order.getOrderItemsNonPromotion()) {
            Product nonProduct = findByProductNameAndPromotionIsNull(orderItem.getProduct().getName());
            nonProduct.decreaseQuantity(orderItem.getOrderQuantity());
        }
    }

    /**
     * 일반 상품인 경우
     */
    public boolean isSingleProduct(String productName) {
        return products.containsKey(createNoPromotionKey(productName)) &&
                !products.containsKey(createPromotionKey(productName));
    }
    
    public String createNoPromotionKey(String productName) {
        return productName + NO_PROMOTION_SUFFIX;
    }

    public String createPromotionKey(String productName) {
        return productName + PROMOTION_SUFFIX;
    }
}