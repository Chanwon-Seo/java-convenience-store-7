package store.parser;

import static store.message.ErrorMessage.INVALID_DATA_FORMAT;

import java.util.List;
import store.dto.ProductDto;
import store.dto.PromotionDto;

public class FileReaderParser {

    private static final String COMMA_DELIMITER = ",";
    private static final String NULL_PROMOTION = "null";
    private static final int EXPECTED_PRODUCT_LENGTH = 4;
    private static final int EXPECTED_PROMOTION_LENGTH = 5;
    private static final int HEADER_SKIP_COUNT = 1;

    public List<ProductDto> parseProduct(List<String> productData) {
        List<String> productRows = removeHeader(productData);
        return productRows.stream()
                .map(this::parseProductRow)
                .toList();
    }

    public List<PromotionDto> parsePromotion(List<String> promotionData) {
        List<String> promotionRows = removeHeader(promotionData);
        return promotionRows.stream()
                .map(this::parsePromotionRow)
                .toList();
    }

    public ProductDto parseProductRow(String row) {
        String[] split = row.split(COMMA_DELIMITER);
        validateDataLength(split, EXPECTED_PRODUCT_LENGTH);
        return createProductDto(split);
    }

    public PromotionDto parsePromotionRow(String row) {
        String[] split = row.split(COMMA_DELIMITER);
        validateDataLength(split, EXPECTED_PROMOTION_LENGTH);
        return createPromotionDto(split);
    }

    public void validateDataLength(String[] split, int expectedLength) {
        if (split.length != expectedLength) {
            throw new IllegalArgumentException(INVALID_DATA_FORMAT.getMessage());
        }
    }

    public ProductDto createProductDto(String[] split) {
        return ProductDto.toProductDto(split[0], split[1], split[2], normalizePromotion(split[3]));
    }

    public PromotionDto createPromotionDto(String[] split) {
        return new PromotionDto(split[0], split[1], split[2], split[3], split[4]);
    }

    public String normalizePromotion(String promotion) {
        if (NULL_PROMOTION.equals(promotion)) {
            return null;
        }
        return promotion;
    }

    public List<String> removeHeader(List<String> rows) {
        return rows.stream().skip(HEADER_SKIP_COUNT).toList();
    }

}
