package store.domain;

import static store.constants.ProductConstants.OUT_OF_STOCK;
import static store.constants.ProductConstants.PRODUCT_DESCRIPTION_PREFIX;
import static store.constants.ProductConstants.UNIT_QUANTITY;
import static store.constants.ProductConstants.UNIT_WON;

import java.text.NumberFormat;
import java.util.Locale;
import store.dto.ProductDto;
import store.exception.ProductException;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    private Product() {
    }

    public Product(ProductDto productDto, Promotion promotion) {
        ProductException.validate(productDto);
        this.name = productDto.name();
        this.price = Integer.parseInt(productDto.price());
        this.quantity = Integer.parseInt(productDto.quantity());
        this.promotion = promotion;
    }

    public boolean isProductName(String productPromotionName) {
        return this.name.equals(productPromotionName);
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
        if (promotion != null) {
            return promotion.getName();
        }
        return "";
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
        return promotion;
    }
}
