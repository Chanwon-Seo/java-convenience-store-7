package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> orderItemsWithPromotion; // 프로모션 상품
    private List<OrderItem> orderItemsNonPromotion; // 일반 상품

    public Order() {
        this.orderItemsNonPromotion = new ArrayList<>();
        this.orderItemsWithPromotion = new ArrayList<>();
    }

    public void addOrderItemsSingleProduct(OrderItem orderItem) {
        orderItemsNonPromotion.add(orderItem);
    }

    public void addOrderItemsWithPromotion(OrderItem orderItem) {
        orderItemsWithPromotion.add(orderItem);
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
