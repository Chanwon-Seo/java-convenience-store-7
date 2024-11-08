package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<OrderItem> orderItems = new ArrayList<>();

    public Cart() {
    }

    public void addItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
