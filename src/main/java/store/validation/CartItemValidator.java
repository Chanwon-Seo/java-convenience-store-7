package store.validation;

import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;
import static store.message.ErrorMessage.QUANTITY_BELOW_MINIMUM;
import static store.message.ErrorMessage.QUANTITY_EXCEEDS_STOCK;

import java.util.List;
import store.domain.Store;
import store.dto.OrderItemDto;

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

    public static void validateOrderItems(List<OrderItemDto> orderItemDtos, Store products) {
        validateProductExists(orderItemDtos, products);
        validateProductStockQuantity(orderItemDtos, products);
    }

    private static void validateProductExists(List<OrderItemDto> orderItemDtos, Store store) {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            if (store.existsByProductName(orderItemDto.productName())) {
                throw new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage());
            }
        }
    }

    private static void validateProductStockQuantity(List<OrderItemDto> orderItemDtos, Store store) {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            if (!store.isTotalQuantityByProductName(orderItemDto)) {
                throw new IllegalArgumentException(QUANTITY_EXCEEDS_STOCK.getMessage());
            }
        }
    }
}
