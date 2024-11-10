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
        if (cartItem.isProductWithPromotion() && cartItem.getProductNonPromotion().getPromotion().isEmpty()) {
            applySingleNonProduct(cartItem, order);
            return;
        }
        if (cartItem.isProductWithPromotion()) {
            applyProductWithPromotion(cartItem, order);
            return;
        }
        applyProductNonPromotion(cartItem, order);
    }

    private void applySingleNonProduct(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem(cartItem.getProductNonPromotion(), cartItem.getQuantity());
        order.addOrderItemsSingleNonProduct(cartItem, orderItem);
    }

    private void applyProductWithPromotion(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem(cartItem.getProductWithPromotion(), cartItem.getQuantity());
        order.addOrderItemsWithPromotion(cartItem, orderItem);
    }

    private void applyProductNonPromotion(CartItem cartItem, Order order) {
        Product productWithPromotion = cartItem.getProductWithPromotion();

        OrderItem orderItemWithPromotion = new OrderItem(cartItem.getProductWithPromotion(), cartItem.getQuantity());
        OrderItem orderItemNonPromotion = new OrderItem(cartItem.getProductNonPromotion(),
                productWithPromotion.getRemainingQuantityNonPromotion(cartItem.getQuantity()));

        order.addOrderItemsNonPromotion(cartItem, orderItemWithPromotion, orderItemNonPromotion);
    }

}