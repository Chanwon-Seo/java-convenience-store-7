package store.service;

import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.Product;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order totalOrder(Cart cart) {
        return createOrder(cart);
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        for (CartItem cartItem : cart.getAllItemsInCart()) {
            addOrderItem(cartItem, order);
        }
        return order;
    }

    private void addOrderItem(CartItem cartItem, Order order) {
        if (cartItem.isProductWithPromotion()) {
            applyProductWithPromotion(cartItem, order);
            return;
        }

        applyProductNonPromotion(cartItem, order);
    }

    private void applyProductNonPromotion(CartItem cartItem, Order order) {
        Product productWithPromotion = cartItem.getProductWithPromotion();

        order.addOrderItemsWithPromotion(new OrderItem(cartItem.getProductWithPromotion(), cartItem.getQuantity()));
        order.addOrderItemsNonPromotion(new OrderItem(cartItem.getProductNonPromotion(),
                productWithPromotion.getRemainingQuantityNonPromotion(cartItem.getQuantity())));
    }

    private void applyProductWithPromotion(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem(cartItem.getProductWithPromotion(), cartItem.getQuantity());
        order.addOrderItemsWithPromotion(orderItem);
    }
}