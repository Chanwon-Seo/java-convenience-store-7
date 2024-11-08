package store.validation;

import static store.message.ErrorMessage.QUANTITY_EXCEEDS_STOCK;

public class OrderItemValidator {
    private static final int MINIMUM_QUANTITY = 0;

    public static void validate(int quantity) {
        validateQuantity(quantity);
    }

    public static void validateQuantity(int quantity) {
        if (quantity <= MINIMUM_QUANTITY) {
            throw new IllegalArgumentException(QUANTITY_EXCEEDS_STOCK.getMessage());
        }
    }
}
