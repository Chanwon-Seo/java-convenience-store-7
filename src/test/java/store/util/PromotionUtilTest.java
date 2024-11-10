package store.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.dto.PromotionDto;

class PromotionUtilTest {
    private PromotionUtil promotionUtil = new PromotionUtil();
    private List<Promotion> promotions;

    @BeforeEach
    void setUp() {
        promotions = new ArrayList<>();
        promotions.add(new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31")));
        promotions.add(new Promotion(new PromotionDto("MD추천상품", "1", "1", "2024-01-01", "2024-12-31")));
        promotions.add(new Promotion(new PromotionDto("반짝할인", "1", "1", "2024-11-01", "2024-11-30")));
    }

    @Test
    void 프로모션_상품_매칭_테스트() {
        String promotionName = "탄산2+1";

        assertDoesNotThrow(() -> promotionUtil.findMatchingPromotion(promotionName, promotions));
    }

    @Test
    void 프로모션_상품_매칭_예외_테스트() {
        String promotionName = "없는프로모션";

        assertThrows(IllegalArgumentException.class,
                () -> promotionUtil.findMatchingPromotion(promotionName, promotions));
    }
}