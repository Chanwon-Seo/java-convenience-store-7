package store.validation;

import static store.message.ErrorMessage.EMPTY_DATE;
import static store.message.ErrorMessage.NON_NUMERIC;

import store.dto.ProductDto;

public abstract class ProductValidator {

    public static void validate(ProductDto productDto) {
        validateEmptyField(productDto.name());
        validateNumericValue(productDto.price());
        validateNumericValue(productDto.quantity());
    }

    public static void validateEmptyField(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(EMPTY_DATE.getMessage());
        }
    }

    public static void validateNumericValue(String value) {
        validateEmptyField(value);
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                throw new IllegalArgumentException(NON_NUMERIC.getMessage());
            }
        }
    }
}
