package store.exception;

import static store.constants.ParserConstants.PRODUCT_HEADER;
import static store.constants.ParserConstants.PROMOTION_HEADER;
import static store.message.ErrorMessage.INVALID_HEADER;

import java.util.List;

public class FileReaderException {
    public static void validateProductHeader(List<String> products) {
        if (!PRODUCT_HEADER.equals(products.getFirst())) {
            throw new IllegalArgumentException(INVALID_HEADER.getMessage());
        }
    }

    public static void validatePromotionHeader(List<String> promotions) {
        if (!PROMOTION_HEADER.equals(promotions.getFirst())) {
            throw new IllegalArgumentException(INVALID_HEADER.getMessage());
        }
    }
}
