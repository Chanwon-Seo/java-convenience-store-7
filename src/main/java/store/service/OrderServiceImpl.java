package store.service;

import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Store;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order totalOrder(Cart cart, Store store) {
        return createOrder(cart, store);
    }

    private Order createOrder(Cart cart, Store store) {
        Order order = new Order();
        for (CartItem cartItem : cart.getAllItemsInCart()) {
            addOrderItem(cartItem, order, store);
        }
        return order;
    }

    private void addOrderItem(CartItem cartItem, Order order, Store store) {
        if (!cartItem.getProduct().isPromotionalProduct()) {
            applySingleNonProduct(cartItem, order);
            return;
        }
        if (cartItem.getProduct().isEligibleForStandardPromotion(cartItem.getQuantity())) {
            applyProductWithPromotion(cartItem, order);
            return;
        }
        applyProductNonPromotion(cartItem, order, store);
    }

    private void applySingleNonProduct(CartItem cartItem, Order order) {
        order.addOrderItemsSingleNonProduct(createOrderItem(cartItem));
    }

    private void applyProductWithPromotion(CartItem cartItem, Order order) {
        order.addOrderItemsWithPromotion(createOrderItem(cartItem));
    }

    private void applyProductNonPromotion(CartItem cartItem, Order order, Store store) {
        Product productWithPromotion = cartItem.getProduct();
        Product productNameAndPromotionIsNull = store.findByProductNameAndPromotionIsNull(cartItem.getProductName());
        OrderItem orderItemWithPromotion = createOrderItem(cartItem);
        OrderItem orderItemNonPromotion = new OrderItem(productNameAndPromotionIsNull,
                productWithPromotion.getRemainingQuantityNonPromotion(cartItem.getQuantity()));

        order.addOrderItemsNonPromotion(orderItemWithPromotion, orderItemNonPromotion);
    }

    private OrderItem createOrderItem(CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }
}