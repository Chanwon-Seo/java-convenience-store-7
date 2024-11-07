package store.domain;

public class OrderItem {
    private String productName;
    private int quantity;

    private OrderItem() {
    }

    public OrderItem(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }
}
