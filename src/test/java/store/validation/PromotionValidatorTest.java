package store.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PromotionValidatorTest {

    @Test
    void 공백_검증_예외_테스트() {
        assertThrows(IllegalArgumentException.class,
                () -> PromotionValidator.validateEmptyField(""));
    }

    @Test
    void 정수형_검증_예외_테스트() {
        assertThrows(IllegalArgumentException.class,
                () -> PromotionValidator.validateNumericValue("문자열"));
    }

    @Test
    void 날짜_포맷_예외_테스트() {
        assertThrows(IllegalArgumentException.class,
                () -> PromotionValidator.validateDateFormat("2024-0101"));
    }

    @Test
    void 시작날짜_및_종료날짜_예외_테스트() {
        assertThrows(IllegalArgumentException.class,
                () -> PromotionValidator.validateDateOrder("2024-12-31","2024-01-01"));
    }

}