package store.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.TestDataUtil;
import store.domain.Store;
import store.dto.CartItemDto;

class CartItemValidatorTest {
    private Store store;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
    }

    @Test
    void 주문_수량_테스트() {
        assertDoesNotThrow(() -> CartItemValidator.validateCartItem(10));
    }

    @Test
    void 주문_수량이_0인_경우_테스트() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CartItemValidator.validateCartItem(0));
        assertEquals(e.getMessage(), "[ERROR] 주문수량이 1이상이어야 합니다.");
    }

    @Test
    void 주문_수량이_음수인_경우_테스트() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CartItemValidator.validateCartItem(-1));
        assertEquals(e.getMessage(), "[ERROR] 주문수량이 1이상이어야 합니다.");
    }

    @Test
    void 재고에_등록된_상품을_주문하는_경우_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", 1));
        cartItemDtos.add(new CartItemDto("사이다", 1));
        CartItemValidator.validateOrderItems(cartItemDtos, store);
    }

    @Test
    void 재고에_등록되지_않은_상품을_주문하는_경우_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("등록되지 않은 상품", 1));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CartItemValidator.validateOrderItems(cartItemDtos, store));

        assertEquals(e.getMessage(), "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @Test
    void 재고_수량_초과_예외_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", 100));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CartItemValidator.validateOrderItems(cartItemDtos, store));
        assertEquals(e.getMessage(), "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

}