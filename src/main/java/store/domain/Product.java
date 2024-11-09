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

    public String getProductDescription() {
        return PRODUCT_DESCRIPTION_PREFIX
                + name + " "
                + getFormattedWinnings(price) + UNIT_WON
                + getFormattedQuantity()
                + getFormattedPromotion();
    }

    public String getFormattedQuantity() {
        if (quantity < 1) {
            return OUT_OF_STOCK;
        }
        return getFormattedWinnings(quantity) + UNIT_QUANTITY;
    }

    public String getFormattedPromotion() {
        return promotion.map(Promotion::getName).orElse("");
    }

    public boolean isEligibleForStandardPromotion(int orderedQuantity) {
        if (promotion.isEmpty()) {
            return false;
        }
        Promotion promotionInfo = promotion.get();
        return orderedQuantity <= quantity
                && getRemainingItemsForPromotion(orderedQuantity, promotionInfo) + promotionInfo.getGet() <= quantity;
    }

    public boolean isEligibleForBonusProduct(int orderedQuantity) {
        if (promotion.isEmpty()) {
            return false;
        }
        Promotion promotionInfo = promotion.get();
        return getRemainingItemsForPromotion(orderedQuantity, promotionInfo) >= promotionInfo.getBuy()
                && orderedQuantity + promotionInfo.getGet() <= quantity;
    }

    public int getRemainingItemsForPromotion(int orderedQuantity, Promotion promotionInfo) {
        return orderedQuantity % promotionInfo.getTotalRequiredQuantity();
    }

    public String getFormattedWinnings(int number) {
        return NumberFormat.getInstance(Locale.KOREA).format(number);
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

    public Promotion getPromotion() {
        return promotion.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PROMOTION.getMessage()));
    }

    public boolean isPromotionalProduct() {
        return promotion.isPresent();
    }
}
