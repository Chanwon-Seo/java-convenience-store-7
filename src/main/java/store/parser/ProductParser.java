package store.parser;

import static store.exception.ParserException.validateDataEmpty;
import static store.exception.ParserException.validateProductHeader;
import static store.message.ErrorMessage.NOT_FOUND_PROMOTION;

import java.util.ArrayList;
import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;

public class ProductParser {
    private static final String NULL_PROMOTION = "null";

    public List<Product> parse(List<String> data, List<Promotion> promotions) {
        validate(data);
        return convertToPromotionList(data, promotions);
    }

    public List<Product> convertToPromotionList(List<String> data, List<Promotion> promotions) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            ProductDto productDto = toProductDto(data.get(i));
            Product product = createProduct(productDto, promotions);
            products.add(product);
        }
        return products;
    }

    public Product createProduct(ProductDto productDto, List<Promotion> promotions) {
        Promotion promotion = getPromotionOrNull(productDto.promotion(), promotions);
        return new Product(productDto, promotion);
    }

    public Promotion getPromotionOrNull(String promotionName, List<Promotion> promotions) {
        if (NULL_PROMOTION.equals(promotionName)) {
            return null;
        }
        return findPromotionByName(promotions, promotionName);
    }

    public Promotion findPromotionByName(List<Promotion> promotions, String promotionName) {
        for (Promotion promotion : promotions) {
            if (promotion.isPromotionName(promotionName)) {
                return promotion;
            }
        }
        throw new IllegalArgumentException(NOT_FOUND_PROMOTION.getMessage());
    }

    public void validate(List<String> data) {
        validateDataEmpty(data);
        validateProductHeader(data);
    }

    public ProductDto toProductDto(String data) {
        return ProductDto.toProductDto(data);
    }

}
