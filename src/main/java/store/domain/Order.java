package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> orderItemsWithPromotion;
    private List<OrderItem> orderItemsNonPromotion;

    public Order() {
        this.orderItemsNonPromotion = new ArrayList<>();
        this.orderItemsWithPromotion = new ArrayList<>();
    }

    public void addOrderItemsWithPromotion(OrderItem orderItem) {
        orderItemsWithPromotion.add(orderItem);
    }

    public void addOrderItemsNonPromotion(OrderItem orderItem) {
        orderItemsNonPromotion.add(orderItem);
    }

    public int totalPriceNonPromotion() {
        return orderItemsNonPromotion.stream()
                .mapToInt(orderItem -> orderItem.getProduct().getPrice() * orderItem.getOrderQuantity())
                .sum();
    }

    public boolean isNoNonPromotionItems() {
        return orderItemsNonPromotion.isEmpty();
    }
}
