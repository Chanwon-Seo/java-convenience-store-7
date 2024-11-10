package store.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.TestDataUtil;
import store.domain.CartItem;
import store.domain.Store;
import store.dto.CartItemDto;

class CartItemParserTest {

    private CartItemParser cartItemParser = new CartItemParser();
    private Store store;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
    }

    @Test
    void 카트_아이템_파싱_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", 1));

        List<CartItem> cartItems = cartItemParser.parse(cartItemDtos, store);

        assertThat(cartItems).hasSize(1);
        assertThat(cartItems).extracting(CartItem::getProductName).containsExactly("콜라");
        assertThat(cartItems).extracting(CartItem::getQuantity).containsExactly(1);
    }

    @Test
    void 카트_여러_아이템_파싱_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", 1));
        cartItemDtos.add(new CartItemDto("사이다", 10));

        List<CartItem> cartItems = cartItemParser.parse(cartItemDtos, store);

        assertThat(cartItems).hasSize(2);
        assertThat(cartItems).extracting(CartItem::getProductName).containsExactly("콜라", "사이다");
        assertThat(cartItems).extracting(CartItem::getQuantity).containsExactly(1, 10);
    }

    @Test
    void 주문_수량이_0인_경우_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", -1));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> cartItemParser.parse(cartItemDtos, store));
        assertEquals(e.getMessage(), "[ERROR] 주문수량이 1이상이어야 합니다.");
    }

    @Test
    void 주문_수량이_음수인_경우_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", 0));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> cartItemParser.parse(cartItemDtos, store));
        assertEquals(e.getMessage(), "[ERROR] 주문수량이 1이상이어야 합니다.");
    }

    @Test
    void 재고_수량_초과_예외_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("콜라", 100));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> cartItemParser.parse(cartItemDtos, store));
        assertEquals(e.getMessage(), "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    @Test
    void 찾을_수_없는_상품_예외_테스트() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItemDtos.add(new CartItemDto("등록되지 않은 상품", 1));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> cartItemParser.parse(cartItemDtos, store));

        assertEquals(e.getMessage(), "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

}