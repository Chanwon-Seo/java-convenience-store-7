package store.domain;

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
