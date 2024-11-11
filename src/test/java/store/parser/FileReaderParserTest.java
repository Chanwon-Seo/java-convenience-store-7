package store.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.ProductDto;

class FileReaderParserTest {
    private final FileReaderParser fileReaderParser = new FileReaderParser();

    @Test
    void 헤더_제거_검증_테스트() {
        List<String> products = new ArrayList<>();
        products.add("name,price,quantity,promotion");
        products.add("콜라,1000,10,탄산2+1");
        String expectedProduct = "콜라,1000,10,탄산2+1";

        List<String> removeHeaderProducts = fileReaderParser.removeHeader(products);

        assertEquals(removeHeaderProducts.size(), 1);
        assertEquals(removeHeaderProducts.get(0), expectedProduct);
    }

    @Test
    void 문자열리스트_파싱_테스트() {
        String row = "콜라,1000,10,탄산2+1";
        String expectedName = "콜라";
        String expectedPrice = "1000";
        String expectedQuantity = "10";
        String expectedPromotion = "탄산2+1";

        ProductDto productDto = fileReaderParser.parseProductRow(row);

        assertEquals(productDto.name(), expectedName);
        assertEquals(productDto.price(), expectedPrice);
        assertEquals(productDto.quantity(), expectedQuantity);
        assertEquals(productDto.promotion(), expectedPromotion);
    }

    @Test
    void 상품_문자열리스트_row_검증_테스트() {
        String[] splitProduct = {"콜라", "1000", "10", "탄산2+1"};
        int expectedLength = 4;

        assertDoesNotThrow(() -> fileReaderParser.validateDataLength(splitProduct, expectedLength));
    }

    @Test
    void 상품_문자열리스트_row_검증_예외_테스트() {
        String[] splitProduct = {"",};
        int expectedLength = 4;

        assertThrows(IllegalStateException.class,
                () -> fileReaderParser.validateDataLength(splitProduct, expectedLength));
    }

    @Test
    void 프로모션_문자열리스트_row_검증_예외_테스트() {
        String[] splitProduct = {"반짝할인","2","1"};
        int expectedLength = 5;

        assertThrows(IllegalStateException.class,
                () -> fileReaderParser.validateDataLength(splitProduct, expectedLength));
    }

    @Test
    void 프로모션_문자열리스트_row_검증_테스트() {
        String[] splitPromotion = {"탄산2+1", "2", "1", "2024-01-01", "2024-12-31"};
        int expectedLength = 5;

        assertDoesNotThrow(() -> fileReaderParser.validateDataLength(splitPromotion, expectedLength));
    }

}