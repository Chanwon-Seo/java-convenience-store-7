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

    public void addOrderItemsSingleNonProduct(OrderItem orderItem) {
        orderItemsNonPromotion.add(orderItem);
    }

    public void addOrderItemsWithPromotion(OrderItem orderItem) {
        orderItemsWithPromotion.add(orderItem);
    }

    public void addOrderItemsNonPromotion(OrderItem orderItemWithPromotion,
                                          OrderItem orderItemNonPromotion) {
        orderItemsWithPromotion.add(orderItemWithPromotion);
        orderItemsNonPromotion.add(orderItemNonPromotion);
    }

    public int totalPriceNonPromotion() {
        return orderItemsNonPromotion.stream()
                .mapToInt(orderItem -> orderItem.getProduct().getPrice() * orderItem.getOrderQuantity())
                .sum();
    }

    public boolean isNoNonPromotionItems() {
        return orderItemsNonPromotion.isEmpty();
    }

    public List<OrderItem> getOrderItemsWithPromotion() {
        return orderItemsWithPromotion;
    }

    public List<OrderItem> getOrderItemsNonPromotion() {
        return orderItemsNonPromotion;
    }
}
