package store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import store.dto.ProductDto;
import store.dto.PromotionDto;

class ProductTest {

    @Test
    void 상품_생성_테스트() {
        ProductDto productDto = new ProductDto("콜라", "1000", "10", null);
        String expectedName = "콜라";
        int expectedPrice = 1000;
        int expectedQuantity = 10;

        Product product = new Product(productDto, Optional.empty());

        assertEquals(product.getName(), expectedName);
        assertEquals(product.getPrice(), expectedPrice);
        assertEquals(product.getQuantity(), expectedQuantity);
        assertThrows(IllegalArgumentException.class,
                () -> product.getPromotionOrElseThrow());
    }

    @Test
    void 상품_상세정보_테스트() {
        ProductDto productDto = new ProductDto("콜라", "1000", "10", null);
        String ExpectedProductDescription = "- 콜라 1,000원 10개 ";

        Product product = new Product(productDto, Optional.empty());

        assertEquals(product.toString(), ExpectedProductDescription);
    }

    @Test
    void 프로모션을_적용할_수_있는_수량_테스트() {
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        ProductDto productDto = new ProductDto("콜라", "1000", "10", "탄산2+1");
        Product product = new Product(productDto, Optional.of(promotion));

        boolean eligibleForBonusProduct = product.isEligibleForStandardPromotion(10);

        assertTrue(eligibleForBonusProduct);
    }

    @Test
    void 프로모션에_적용할_수_없는_테스트() {
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        ProductDto productDto = new ProductDto("콜라", "1000", "10", "탄산2+1");
        Product product = new Product(productDto, Optional.of(promotion));

        boolean eligibleForBonusProduct = product.isEligibleForStandardPromotion(12);

        assertFalse(eligibleForBonusProduct);
    }

    @Test
    void 프로모션이_없는_상품_테스트() {
        ProductDto productDto = new ProductDto("콜라", "1000", "10", "탄산2+1");
        Product product = new Product(productDto, Optional.empty());

        boolean eligibleForBonusProduct = product.isEligibleForStandardPromotion(2);

        assertFalse(eligibleForBonusProduct);
    }

    @Test
    void 증정상품을_제공할_수_있는_경우_테스트() {
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        ProductDto productDto = new ProductDto("콜라", "1000", "10", "탄산2+1");
        Product product = new Product(productDto, Optional.of(promotion));

        boolean eligibleForBonusProduct = product.isEligibleForBonusProduct(2);

        assertTrue(eligibleForBonusProduct);
    }

    @Test
    void 증정상품을_제공할_수_없는_경우_테스트() {
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        ProductDto productDto = new ProductDto("콜라", "1000", "10", "탄산2+1");
        Product product = new Product(productDto, Optional.of(promotion));

        boolean eligibleForBonusProduct = product.isEligibleForBonusProduct(3);

        assertFalse(eligibleForBonusProduct);
    }

    @Test
    void 증정상품을_제공할_수_없는_경우_테스트1() {
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        ProductDto productDto = new ProductDto("콜라", "1000", "10", "탄산2+1");
        Product product = new Product(productDto, Optional.of(promotion));

        boolean eligibleForBonusProduct = product.isEligibleForBonusProduct(10);

        assertFalse(eligibleForBonusProduct);
    }

    @Test
    void 프로모션_혜택을_받지_못하는_수량_테스트() {
        Promotion promotion = new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31"));
        ProductDto productDto = new ProductDto("콜라", "1000", "7", "탄산2+1");
        Product product = new Product(productDto, Optional.of(promotion));
        int expectedQuantity = 4;

        int totalQuantity = product.calculateQuantityAfterPromotion(10);

        assertEquals(totalQuantity, expectedQuantity);
    }

}