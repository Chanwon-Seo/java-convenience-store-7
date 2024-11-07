package store.exception;

import static store.constants.ParserConstants.PRODUCT_HEADER;
import static store.constants.ParserConstants.PROMOTION_HEADER;
import static store.message.ErrorMessage.INVALID_HEADER;

import java.util.List;

public class FileReaderException {
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
