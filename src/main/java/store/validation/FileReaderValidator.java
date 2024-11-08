package store.validation;

import static store.message.ErrorMessage.INVALID_HEADER;

import java.util.List;

public abstract class FileReaderValidator {
    private static final String PRODUCT_HEADER = "name,price,quantity,promotion";
    private static final String PROMOTION_HEADER = "name,buy,get,start_date,end_date";

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
