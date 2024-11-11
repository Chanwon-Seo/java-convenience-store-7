package store.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.TestDataUtil;
import store.dto.CartItemDto;
import store.dto.ProductDto;
import store.dto.PromotionDto;

class StoreTest {
    private Store store;

    @BeforeEach
    void setUp() {
        store = TestDataUtil.createStore();
    }

    @Test
    void 재고_총_수량_테스트() {
        int expectedProductSize = 18;
        List<Product> findAllProductInfo = store.findAll();

        assertEquals(findAllProductInfo.size(), expectedProductSize);
    }

    @Test
    void 특정_상품의_프로모션을_찾는_테스트() {
        String findProductName = "콜라";
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        Product expectedProducts = new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.of(promotion));

        Product product = store.findByProductNameAndPromotionIsNotNullOrThrow(findProductName);

        assertEquals(product.getName(), expectedProducts.getName());
        assertEquals(product.getPrice(), expectedProducts.getPrice());
        assertEquals(product.getQuantity(), expectedProducts.getQuantity());
        assertDoesNotThrow(product::getPromotionOrElseThrow);
    }

    @Test
    void 특정_상품리스트를_찾는_테스트() {
        String findProductName = "콜라";
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        List<Product> expectedProducts = List.of(
                new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.of(promotion)),
                new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.empty()));

        Product product = store.findByProductNameAndPromotionIsNotNull(findProductName);

        assertEquals(2, expectedProducts.size());
        assertEquals(product.getName(), expectedProducts.getFirst().getName());
    }

    @Test
    void 최대_구매가능_수량_테스트() {
        CartItemDto cartItemDto = new CartItemDto("콜라", 10);

        boolean totalQuantityByProductName = store.isTotalQuantityByProductName(cartItemDto);

        assertTrue(totalQuantityByProductName);
    }

    @Test
    void 최대_구매가능_수량을_초과한_경우_예외_테스트() {
        CartItemDto cartItemDto = new CartItemDto("콜라", 100);
        boolean totalQuantityByProductName = store.isTotalQuantityByProductName(cartItemDto);

        assertFalse(totalQuantityByProductName);
    }

}