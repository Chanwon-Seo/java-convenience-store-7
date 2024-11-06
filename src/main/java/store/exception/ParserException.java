package store.exception;

import static store.message.ErrorMessage.EMPTY_DATA;
import static store.message.ErrorMessage.INVALID_HEADER;
import static store.parser.ParserConstants.PRODUCT_HEADER;
import static store.parser.ParserConstants.PROMOTION_HEADER;

import java.util.List;

public class ParserException {
    public static void validateDataEmpty(List<String> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DATA.getMessage());
        }
    }

    public static void validateProductHeader(List<String> data) {
        if (!data.getFirst().equals(PRODUCT_HEADER)) {
            throw new IllegalArgumentException(INVALID_HEADER.getMessage());
        }
    }

    public static void validatePromotionHeader(List<String> data) {
        if (!data.getFirst().equals(PROMOTION_HEADER)) {
            throw new IllegalArgumentException(INVALID_HEADER.getMessage());
        }
    }
}
