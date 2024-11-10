package store.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;
import store.dto.PromotionDto;

class ProductParserTest {
    private ProductParser productParser = new ProductParser();
    private List<Promotion> promotions;

    @BeforeEach
    void setUp() {
        promotions = new ArrayList<>();
        promotions.add(new Promotion(new PromotionDto("탄산2+1", "2", "1", "2024-01-01", "2024-12-31")));
        promotions.add(new Promotion(new PromotionDto("MD추천상품", "1", "1", "2024-01-01", "2024-12-31")));
        promotions.add(new Promotion(new PromotionDto("반짝할인", "1", "1", "2024-11-01", "2024-11-30")));
    }

    @Test
    void 상품_및_파싱_테스트() {
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(new ProductDto("콜라", "1000", "10", "탄산2+1"));
        productDtos.add(new ProductDto("콜라", "1000", "10", null));

        Map<String, List<Product>> expectedMap = new LinkedHashMap<>();
        List<Product> expectedProducts = new ArrayList<>();
        expectedMap.put("콜라", expectedProducts);
        expectedProducts.add(new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.of(promotions.get(0))));
        expectedProducts.add(new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.empty()));

        Map<String, List<Product>> productsWithPromotions = productParser.parse(productDtos, promotions);

        assertEquals(expectedMap.keySet(), productsWithPromotions.keySet());
        assertEquals(expectedMap.values().toString(), productsWithPromotions.values().toString());
    }

    @Test
    void 프로모션_상품은_존재하지만_일반재고는_없는_경우() {
        // Given
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(new ProductDto("오렌지주스", "1800", "9", "MD추천상품"));

        ProductDto productDto = new ProductDto("오렌지주스", "1800", "9", "MD추천상품");
        Product productWithPromotion = new Product(productDto, Optional.of(promotions.get(1)));
        Product productWithoutPromotion = new Product(productDto, Optional.empty());

        Map<String, List<Product>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("오렌지주스", List.of(productWithPromotion, productWithoutPromotion));

        Map<String, List<Product>> productsWithPromotions = productParser.parse(productDtos, promotions);

        List<Product> productList = productsWithPromotions.values().stream()
                .flatMap(List::stream)
                .toList();
        int expectedProductSize = 2;
        String expectedName = "오렌지주스";
        int expectedPrice = 1800;
        int expectedQuantity = 0;

        assertEquals(expectedMap.keySet(), productsWithPromotions.keySet());
        assertEquals(productList.size(), expectedProductSize);
        assertEquals(productList.get(1).getName(), expectedName);
        assertEquals(productList.get(1).getPrice(), expectedPrice);
        assertEquals(productList.get(1).getQuantity(), expectedQuantity);
    }

}