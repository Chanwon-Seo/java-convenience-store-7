package store.domain;

import static store.validation.OrderItemValidator.validate;

import java.util.List;

public class CartItem {
    private String productName;
    private int quantity;

    public CartItem(String productName, int quantity) {
        validate(quantity);
        this.productName = productName;
        this.quantity = quantity;
    }

    public void increaseQuantity(int updateQuantity) {
        this.quantity += updateQuantity;
    }

    public void decreaseQuantity(int updateQuantity) {
        this.quantity -= updateQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
