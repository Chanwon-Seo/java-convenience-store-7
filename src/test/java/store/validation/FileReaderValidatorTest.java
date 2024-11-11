package store.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class FileReaderValidatorTest {
    @Test
    void 상품_파일_헤더_검증_테스트() {
        List<String> products = new ArrayList<>();
        products.add("name,price,quantity,promotion");

        assertDoesNotThrow(() -> FileReaderValidator.validateProductHeader(products));
    }

    @Test
    void 상품_파일_헤더_불일치_검증_테스트() {
        List<String> products = new ArrayList<>();
        products.add("상품,불일치,헤더");

        assertThrows(IllegalStateException.class,
                () -> FileReaderValidator.validateProductHeader(products));
    }

    @Test
    void 프로모션_파일_헤더_검증_테스트() {
        List<String> promotions = new ArrayList<>();
        promotions.add("name,buy,get,start_date,end_date");

        assertDoesNotThrow(() -> FileReaderValidator.validatePromotionHeader(promotions));
    }

    @Test
    void 프로모션_파일_헤더_불일치_검증_테스트() {
        List<String> promotions = new ArrayList<>();
        promotions.add("프로모션,불일치,헤더");

        assertThrows(IllegalStateException.class,
                () -> FileReaderValidator.validatePromotionHeader(promotions));
    }
}