package store.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import store.dto.PromotionDto;

class PromotionTest {

    @Test
    void 프로모션_생성_테스트() {
        PromotionDto promotionDto = new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31");
        String expectedName = "탄산2+1";
        int expectedBuy = 2;
        int expectedGet = 1;
        LocalDateTime expectedStartDate = LocalDate.of(2024, 1, 1).atStartOfDay();
        LocalDateTime expectedEndDate = LocalDate.of(2024, 12, 31).atStartOfDay();

        Promotion promotion = new Promotion(promotionDto);

        assertEquals(promotion.getName(), expectedName);
        assertEquals(promotion.getBuy(), expectedBuy);
        assertEquals(promotion.getGet(), expectedGet);
        assertEquals(promotion.getStartDate(), expectedStartDate);
        assertEquals(promotion.getEndDate(), expectedEndDate);
    }

}