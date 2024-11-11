package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.TestDataUtil;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Order;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Store;

class OrderServiceImplTest {

    private OrderServiceImpl orderService = new OrderServiceImpl();
    private Store store;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
    }

    @Test
    void 일반_상품_항목에_추가_테스트() {
        Product product = store.findByProductNameAndPromotionIsNull("에너지바");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product, 5));
        Cart cart = new Cart();
        cart.addItem(cartItems);

        Order order = orderService.totalOrder(cart, store);

        assertNotNull(order);
        assertEquals("에너지바", order.getOrderItemsNonPromotion().getFirst().getProduct().getName());
        assertEquals(5, order.getOrderItemsNonPromotion().getFirst().getOrderQuantity());
    }

    @Test
    void 프로모션_상품을_주문_항목에_추가_테스트() {
        Product product = store.findByProductNameAndPromotionIsNotNull("콜라");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product, 5));
        Cart cart = new Cart();
        cart.addItem(cartItems);

        Order order = orderService.totalOrder(cart, store);

        assertNotNull(order);
        assertEquals(5, order.getOrderItemsWithPromotion().getFirst().getOrderQuantity());
    }

    @Test
    void 프로모션_상품_재고를_초과하여_일반_상품_항목에_추가_테스트() {
        Product product = store.findByProductNameAndPromotionIsNotNull("콜라");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product, 20));
        Cart cart = new Cart();
        cart.addItem(cartItems);

        Order order = orderService.totalOrder(cart, store);

        assertNotNull(order);
        assertEquals("콜라", order.getOrderItemsWithPromotion().getFirst().getProduct().getName());
        //프로모션 상품 항목
        assertEquals(10, order.getOrderItemsWithPromotion().getFirst().getOrderQuantity());
        //일반 상품 항목
        assertEquals(10, order.getOrderItemsNonPromotion().getFirst().getOrderQuantity());
    }

    @Test
    void 프로모션_상품_및_일반_상품_추가_테스트() {
        Product product1 = store.findByProductNameAndPromotionIsNotNull("콜라");
        Product product2 = store.findByProductNameAndPromotionIsNull("에너지바");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product1, 20));
        cartItems.add(new CartItem(product2, 2));
        Cart cart = new Cart();
        cart.addItem(cartItems);

        Order order = orderService.totalOrder(cart, store);

        assertNotNull(order);
        // 프로모션 상품 관련 검증
        assertEquals("콜라", order.getOrderItemsWithPromotion().get(0).getProduct().getName());
        assertEquals(10, order.getOrderItemsWithPromotion().get(0).getOrderQuantity());

        // 일반 상품 관련 검증
        assertEquals("에너지바", order.getOrderItemsNonPromotion().get(1).getProduct().getName());
        assertEquals(10, order.getOrderItemsNonPromotion().get(0).getOrderQuantity());
        assertEquals(2, order.getOrderItemsNonPromotion().get(1).getOrderQuantity());
    }

}
