package store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.TestDataUtil;

class CartItemTest {
    private Store store;
    private Product product;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
        product = store.findByProductNameAndPromotionIsNotNull("콜라");
    }

    @Test
    void 카트아이템_생성_테스트() {
        String expectedProductName = "콜라";
        int expectedQuantity = 10;

        CartItem cartItem = new CartItem(product, 10);

        assertEquals(cartItem.getProduct().getName(), expectedProductName);
        assertEquals(cartItem.getQuantity(), expectedQuantity);
    }

    @Test
    void 카트아이템_추가_테스트() {
        int expectedQuantity = 1;

        CartItem cartItem = new CartItem(product, 8);
        cartItem.increaseQuantity(1);

        assertEquals(cartItem.getFreeQuantity(), expectedQuantity);
    }

    @Test
    void 카트아이템_감소_테스트() {
        int expectedQuantity = 6;

        CartItem cartItem = new CartItem(product, 10);
        cartItem.decreaseQuantity(4);

        assertEquals(cartItem.getQuantity(), expectedQuantity);
    }

    @Test
    void 프로모션_미적용_상품_삭제_테스트() {
        CartItem cartItem = new CartItem(product, 10);

        cartItem.decreaseQuantity(4);

        assertEquals(cartItem.getQuantity(), 6);
    }

    @Test
    void 프로모션_미적용_상품_삭제_예외_테스트() {
        CartItem cartItem = new CartItem(product, 4);

        assertThrows(IllegalArgumentException.class,
                () -> cartItem.decreaseQuantity(4));
    }
}