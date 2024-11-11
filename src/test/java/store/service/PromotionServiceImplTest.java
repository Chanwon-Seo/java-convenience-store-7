package store.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.Application;
import store.TestDataUtil;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Store;

class PromotionServiceImplTest extends NsTest {

    private final PromotionServiceImpl promotionService = new PromotionServiceImpl();
    private Store store;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
    }

    @Test
    void 프로모션_상품_증정_테스트() {
        Product product = store.findByProductNameAndPromotionIsNotNull("콜라");
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product, 3));
        Cart cart = new Cart();
        cart.addItem(cartItems);

        promotionService.setFreeProductQuantity(cart, store);

        CartItem first = cart.getAllItemsInCart().getFirst();
        assertEquals(3, first.getQuantity());
        assertEquals(1, first.getFreeQuantity());
    }

    @Test
    void 일반_상품_테스트() {
        Product product = store.findByProductNameAndPromotionIsNull("물");
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product, 3));

        Cart cart = new Cart();
        cart.addItem(cartItems);

        promotionService.setFreeProductQuantity(cart, store);
        CartItem first = cart.getAllItemsInCart().getFirst();

        assertEquals(3, first.getQuantity());
        assertEquals(0, first.getFreeQuantity());
    }

    @Test
    void 증정상품_추가_여부_테스트() {
        String simulatedUserInput = "Y";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        Product product = store.findByProductNameAndPromotionIsNotNull("사이다");
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(product, 2));

        Cart cart = new Cart();
        cart.addItem(cartItems);

        promotionService.setFreeProductQuantity(cart, store);

        CartItem first = cart.getAllItemsInCart().getFirst();

        assertEquals(2, first.getQuantity());
        assertEquals(1, first.getFreeQuantity());

        assertThat(output()).contains(
                "현재 사이다은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
        );
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}