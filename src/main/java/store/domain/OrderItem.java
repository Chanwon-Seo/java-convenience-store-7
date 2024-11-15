package store.domain;

public class OrderItem {
    private final Product product;
    private final int orderQuantity;

    public OrderItem(Product product, int orderQuantity) {
        this.product = product;
        this.orderQuantity = orderQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }
}
