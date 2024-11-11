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
        List<Product> findProduct = store.findByProductName(cartItem.getProductName());
        if (applyPromotionalProduct(cartItem, order, findProduct)) {
            return;
        }
        applyMixedProduct(cartItem, order, findProduct);
    }

    private boolean applyPromotionalProduct(CartItem cartItem, Order order, List<Product> findProduct) {
        if (findProduct.getFirst().getQuantity() >= cartItem.getQuantity()) {
            order.addOrderItemsWithPromotion(new OrderItem(findProduct.getFirst(), cartItem.getQuantity()));
            return true;
        }
        return false;
    }

    private void applyMixedProduct(CartItem cartItem, Order order, List<Product> findProduct) {
        int cartItemQuantity = cartItem.getQuantity();

        Product promotionProduct = findProduct.getFirst();
        OrderItem orderItem = new OrderItem(promotionProduct, promotionProduct.getQuantity());
        OrderItem orderItem1 = new OrderItem(findProduct.getLast(),
                cartItemQuantity - promotionProduct.getQuantity());
        order.addOrderItemsWithPromotion(orderItem);
        order.addOrderItemsSingleProduct(orderItem1);
    }

    private OrderItem createOrderItem(CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }
}