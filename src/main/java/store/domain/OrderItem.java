package store.domain;

import static store.validation.OrderItemValidator.validate;

public class OrderItem {
    private String productName;
    private int quantity;

    public OrderItem(String productName, int quantity) {
        validate(quantity);
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int updateQuantity) {
        this.quantity += updateQuantity;
    }
}
