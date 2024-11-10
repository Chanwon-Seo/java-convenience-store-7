package store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.TestDataUtil;

class CartItemTest {
    private Store store;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
    }

    @Test
    void 카트아이템_생성_테스트() {
        List<Product> products = store.findProductsByName("콜라");
        String expectedProductName = "콜라";
        int expectedQuantity = 10;

        CartItem cartItem = new CartItem(products, 10);

        assertEquals(cartItem.getProductName(), expectedProductName);
        assertEquals(cartItem.getQuantity(), expectedQuantity);
    }

    @Test
    void 카트아이템_추가_테스트() {
        List<Product> products = store.findProductsByName("콜라");
        int expectedQuantity = 9;

        CartItem cartItem = new CartItem(products, 8);
        cartItem.increaseQuantity(1);

        assertEquals(cartItem.getQuantity(), expectedQuantity);
    }

    @Test
    void 카트아이템_감소_테스트() {
        List<Product> products = store.findProductsByName("콜라");
        int expectedQuantity = 6;

        CartItem cartItem = new CartItem(products, 10);
        cartItem.decreaseQuantity(4);

        assertEquals(cartItem.getQuantity(), expectedQuantity);
     }

    @Test
    void 프로모션_미적용_상품_삭제_테스트() {
        List<Product> products = store.findProductsByName("콜라");
        String expectedPromotionName = "탄산2+1";

        CartItem cartItem = new CartItem(products, 10);
        cartItem.removeCartItem();

        List<Product> cartItemProducts = cartItem.getProducts();
        assertEquals(cartItemProducts.size(), 1);
        assertEquals(cartItemProducts.getFirst().getPromotionOrElseThrow().getName(), expectedPromotionName);
    }
}