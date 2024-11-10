package store.validation;

import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;
import static store.message.ErrorMessage.QUANTITY_BELOW_MINIMUM;
import static store.message.ErrorMessage.QUANTITY_EXCEEDS_STOCK;

import java.util.List;
import store.domain.Store;
import store.dto.CartItemDto;

public abstract class CartItemValidator {
    private static final int MINIMUM_QUANTITY = 0;

    public static void validateCartItem(int quantity) {
        validateQuantity(quantity);
    }

    public static void validateQuantity(int quantity) {
        if (quantity <= MINIMUM_QUANTITY) {
            throw new IllegalArgumentException(QUANTITY_BELOW_MINIMUM.getMessage());
        }
    }

    public static void validateOrderItems(List<CartItemDto> cartItemDtos, Store products) {
        validateProductExists(cartItemDtos, products);
        validateProductStockQuantity(cartItemDtos, products);
    }

    private static void validateProductExists(List<CartItemDto> cartItemDtos, Store store) {
        for (CartItemDto cartItemDto : cartItemDtos) {
            if (store.existsByProductName(cartItemDto.productName())) {
                throw new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage());
            }
        }
    }

    private static void validateProductStockQuantity(List<CartItemDto> cartItemDtos, Store store) {
        for (CartItemDto cartItemDto : cartItemDtos) {
            if (!store.isTotalQuantityByProductName(cartItemDto)) {
                throw new IllegalArgumentException(QUANTITY_EXCEEDS_STOCK.getMessage());
            }
        }
    }
}
