package store.domain;

import static store.message.ErrorMessage.INSUFFICIENT_STOCK_ERROR;

import store.validation.CartItemValidator;

public class CartItem {
    private final Product product;
    private int quantity;
    private int freeQuantity;

    public CartItem(Product product, int quantity) {
        CartItemValidator.validateCartItem(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    public void increaseQuantity(int updateQuantity) {
        this.freeQuantity += updateQuantity;
    }

    public void decreaseQuantity(int updateQuantity) {
        this.quantity -= updateQuantity;
        if (quantity <= 0) {
            throw new IllegalArgumentException(INSUFFICIENT_STOCK_ERROR.getMessage());
        }
    }

    public String getProductName() {
        return product.getName();
    }

    public int totalPrice() {
        return product.getPrice() * getTotalQuantity();
    }

    public int getTotalFreePrice() {
        return product.getPrice() * freeQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalQuantity() {
        return quantity + freeQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }
}
