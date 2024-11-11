package store.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductValidatorTest {
    @Test
    void 공백_검증_예외_테스트() {
        assertThrows(IllegalArgumentException.class,
                () -> ProductValidator.validateEmptyField(""));
    }

    @Test
    void 정수형_검증_예외_테스트() {
        assertThrows(IllegalArgumentException.class,
                () -> ProductValidator.validateNumericValue("문자열"));
    }

}