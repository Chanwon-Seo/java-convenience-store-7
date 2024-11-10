package store.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private Map<String, Integer> orderInfo;
    private List<OrderItem> orderItemsWithPromotion;
    private List<OrderItem> orderItemsNonPromotion;

    public Order() {
        this.orderInfo = new LinkedHashMap<>();
        this.orderItemsNonPromotion = new ArrayList<>();
        this.orderItemsWithPromotion = new ArrayList<>();
    }

    public void addOrderItemsSingleNonProduct(CartItem cartItem, OrderItem orderItem) {
        orderInfo.put(cartItem.getProductName(), cartItem.getQuantity());
        orderItemsNonPromotion.add(orderItem);
    }

    public void addOrderItemsWithPromotion(CartItem cartItem, OrderItem orderItem) {
        orderInfo.put(cartItem.getProductName(), cartItem.getQuantity());
        orderItemsWithPromotion.add(orderItem);
    }

    public void addOrderItemsNonPromotion(CartItem cartItem, OrderItem orderItemWithPromotion,
                                          OrderItem orderItemNonPromotion) {
        orderInfo.put(cartItem.getProductName(), cartItem.getQuantity());
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
