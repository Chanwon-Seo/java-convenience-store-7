package store.domain;

import static store.constants.ProductConstants.OUT_OF_STOCK;
import static store.constants.ProductConstants.PRODUCT_DESCRIPTION_PREFIX;
import static store.constants.ProductConstants.UNIT_QUANTITY;
import static store.constants.ProductConstants.UNIT_WON;
import static store.message.ErrorMessage.NOT_FOUND_PROMOTION;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import store.dto.ProductDto;
import store.validation.ProductValidator;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private Optional<Promotion> promotion;

    private Product() {
    }

    public Product(ProductDto productDto, Optional<Promotion> promotion) {
        ProductValidator.validate(productDto);
        this.name = productDto.name();
        this.price = Integer.parseInt(productDto.price());
        this.quantity = Integer.parseInt(productDto.quantity());
        this.promotion = promotion;
    }

    /**
     * 프로모션을 적용 여부 검증
     */
    public boolean isEligibleForStandardPromotion(int orderedQuantity) {
        if (promotion.isEmpty()) {
            return false;
        }
        Promotion promotionInfo = promotion.get();
        return orderedQuantity <= quantity
                && getRemainingItemsForPromotion(orderedQuantity, promotionInfo) + promotionInfo.getGet() <= quantity;
    }

    /**
     * 보너스 상품이 적용 여부 검증
     */
    public boolean isEligibleForBonusProduct(int orderedQuantity) {
        if (promotion.isEmpty()) {
            return false;
        }
        Promotion promotionInfo = promotion.get();
        return getRemainingItemsForPromotion(orderedQuantity, promotionInfo) >= promotionInfo.getBuy()
                && orderedQuantity + promotionInfo.getGet() <= quantity;
    }

    /**
     * 프로모션 조건을 만족하는 남은 수량을 계산
     */
    public int getRemainingItemsForPromotion(int orderedQuantity, Promotion promotionInfo) {
        return orderedQuantity % promotionInfo.getTotalRequiredQuantity();
    }

    /**
     * 보너스 상품의 수량을 계산
     */
    public int calculateBonusQuantity(int orderedQuantity, Promotion promotion) {
        return (orderedQuantity / promotion.getTotalRequiredQuantity()) * promotion.getGet();
    }

    /**
     * 프로모션이 적용된 후, 실제 주문 가능한 수량을 계산
     */
    public int calculateQuantityAfterPromotion(int orderedQuantity) {
        Promotion promotionInfo = getPromotionOrElseThrow();
        int requiredQuantity = promotionInfo.getTotalRequiredQuantity();
        return (orderedQuantity - quantity) + (quantity % requiredQuantity);
    }

    /**
     * 프로모션이 존재하지 않으면 예외 발생
     */
    public Promotion getPromotionOrElseThrow() {
        return promotion.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PROMOTION.getMessage()));
    }

    /**
     * 값을 가지고 있는 주체이기 때문에 상품 리스트를 포맷을 합니다.
     */
    @Override
    public String toString() {
        return PRODUCT_DESCRIPTION_PREFIX
                + name + " "
                + getFormatKoreanLocale(price) + UNIT_WON
                + getFormattedQuantity()
                + getFormattedPromotion();
    }

    public String getFormattedQuantity() {
        if (quantity < 1) {
            return OUT_OF_STOCK;
        }
        return getFormatKoreanLocale(quantity) + UNIT_QUANTITY;
    }

    public String getFormatKoreanLocale(int number) {
        return NumberFormat.getInstance(Locale.KOREA).format(number);
    }

    public String getFormattedPromotion() {
        return promotion.map(Promotion::getName).orElse("");
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

    public boolean isPromotionalProduct() {
        return promotion.isPresent();
    }

    public void decreaseQuantity(int orderQuantity) {
        this.quantity -= orderQuantity;
    }
}
