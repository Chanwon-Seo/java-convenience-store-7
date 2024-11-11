package store.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;
import store.dto.PromotionDto;

class ProductParserTest {
    private static final String PROMOTION_SUFFIX = "_PROMO";
    private static final String NON_PROMOTION_SUFFIX = "_NO_PROMO";
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

        Map<String, Product> expectedMap = new LinkedHashMap<>();

        expectedMap.put("콜라" + PROMOTION_SUFFIX,
                new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.of(promotions.get(0))));
        expectedMap.put("콜라" + NON_PROMOTION_SUFFIX,
                new Product(new ProductDto("콜라", "1000", "10", "탄산2+1"), Optional.empty()));

        Map<String, Product> productsWithPromotions = productParser.parse(productDtos, promotions);

        assertEquals(expectedMap.keySet(), productsWithPromotions.keySet());
        assertEquals(expectedMap.values().toString(), productsWithPromotions.values().toString());
    }

    @Test
    void 프로모션_상품은_존재하지만_일반재고는_없는_경우() {
        List<ProductDto> productDtos = new ArrayList<>();
        productDtos.add(new ProductDto("오렌지주스", "1800", "9", "MD추천상품"));

        ProductDto productDto = new ProductDto("오렌지주스", "1800", "9", "MD추천상품");
        Product productWithPromotion = new Product(productDto, Optional.of(promotions.get(1)));
        Product productWithoutPromotion = new Product(productDto, Optional.empty());

        Map<String, Product> expectedMap = new LinkedHashMap<>();
        expectedMap.put("오렌지주스" + PROMOTION_SUFFIX, productWithPromotion);
        expectedMap.put("오렌지주스" + NON_PROMOTION_SUFFIX, productWithoutPromotion);

        Map<String, Product> productsWithPromotions = productParser.parse(productDtos, promotions);

        List<Product> productList = productsWithPromotions.values().stream()
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