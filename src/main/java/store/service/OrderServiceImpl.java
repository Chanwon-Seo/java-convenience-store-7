package store.service;

import java.util.List;
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
        if (store.isSingleProduct(cartItem.getProductName())) {
            applyProductNonPromotion(cartItem, order);
            return;
        }
        applyProductWithPromotion(cartItem, order, store);
    }

    private void applyProductNonPromotion(CartItem cartItem, Order order) {
        order.addOrderItemsSingleProduct(createOrderItem(cartItem));
    }

    private void applyProductWithPromotion(CartItem cartItem, Order order, Store store) {
        List<Product> products = store.findByProductName(cartItem.getProductName());
        int remainingQuantity = cartItem.getTotalQuantity();

        for (Product product : products) {
            if (remainingQuantity <= 0) {
                break;
            }
            int quantityToAdd = Math.min(remainingQuantity, product.getQuantity());
            order.addOrderItemsWithPromotion(new OrderItem(product, quantityToAdd));
            remainingQuantity -= quantityToAdd;
        }
    }


    private OrderItem createOrderItem(CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }
}