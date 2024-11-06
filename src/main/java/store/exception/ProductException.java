package store.exception;

import static store.message.ErrorMessage.EMPTY_DATE;
import static store.message.ErrorMessage.NON_NUMERIC;

import store.dto.ProductDto;

public abstract class ProductException {

    public static void validate(ProductDto productDto) {
        validateEmptyField(productDto.name());
        validateNumericValue(productDto.price());
        validateNumericValue(productDto.quantity());
    }

    public static void validateEmptyField(String date) {
        if (date.isBlank()) {
            throw new IllegalArgumentException(EMPTY_DATE.getMessage());
        }
    }

    public static void validateNumericValue(String data) {
        validateEmptyField(data);
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isDigit(data.charAt(i))) {
                throw new IllegalArgumentException(NON_NUMERIC.getMessage());
            }
        }
    }
}
