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

    /**
     * 주문을 생성하고 장바구니의 모든 아이템을 주문 항목에 추가
     */
    private Order createOrder(Cart cart, Store store) {
        Order order = new Order();
        for (CartItem cartItem : cart.getAllItemsInCart()) {
            addOrderItem(cartItem, order, store);
        }
        return order;
    }

    /**
     * 장바구니 아이템을 주문 항목에 추가
     */
    private void addOrderItem(CartItem cartItem, Order order, Store store) {
        if (store.isSingleProduct(cartItem.getProductName())) {
            applyProductNonPromotion(cartItem, order);
            return;
        }
        applyProductWithPromotion(cartItem, order, store);
    }

    /**
     * 프로모션 없이 일반 상품을 주문 항목에 추가
     */
    private void applyProductNonPromotion(CartItem cartItem, Order order) {
        order.addOrderItemsSingleProduct(createOrderItem(cartItem));
    }

    /**
     * 프로모션이 적용된 상품을 주문 항목에 추가
     */
    private void applyProductWithPromotion(CartItem cartItem, Order order, Store store) {
        List<Product> findProduct = store.findByProductName(cartItem.getProductName());
        if (applyPromotionalProduct(cartItem, order, findProduct)) {
            return;
        }
        applyMixedProduct(cartItem, order, findProduct);
    }

    /**
     * 프로모션이 적용된 상품을 주문 항목에 추가
     */
    private boolean applyPromotionalProduct(CartItem cartItem, Order order, List<Product> findProduct) {
        if (findProduct.getFirst().getQuantity() >= cartItem.getQuantity()) {
            order.addOrderItemsWithPromotion(new OrderItem(findProduct.getFirst(), cartItem.getQuantity()));
            return true;
        }
        return false;
    }

    /**
     * 혼합된 프로모션 상품을 주문 항목에 추가
     */
    private void applyMixedProduct(CartItem cartItem, Order order, List<Product> findProduct) {
        int cartItemQuantity = cartItem.getQuantity();

        Product promotionProduct = findProduct.getFirst();
        OrderItem orderItem = new OrderItem(promotionProduct, promotionProduct.getQuantity());
        OrderItem orderItem1 = new OrderItem(findProduct.getLast(),
                cartItemQuantity - promotionProduct.getQuantity());
        order.addOrderItemsWithPromotion(orderItem);
        order.addOrderItemsSingleProduct(orderItem1);
    }

    /**
     * 장바구니 아이템에 대한 주문 항목을 생성
     */
    private OrderItem createOrderItem(CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }
}