package store.parser;

import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;
import static store.message.ErrorMessage.QUANTITY_EXCEEDS_STOCK;

import java.util.List;
import store.domain.CartItem;
import store.domain.Store;
import store.dto.OrderItemDto;

public class CartItemParser {

    public List<CartItem> parse(List<OrderItemDto> orderItemDtos, Store store) {
        validator(orderItemDtos, store);
        return orderItemDtos.stream()
                .map(orderItemDto -> new CartItem(orderItemDto.productName(), orderItemDto.quantity()))
                .toList();
    }

    public void validator(List<OrderItemDto> orderItemDtos, Store products) {
        validateProductExistence(orderItemDtos, products);
        validateProductQuantity(orderItemDtos, products);
    }


    private void validateProductExistence(List<OrderItemDto> orderItemDtos, Store store) {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            if (!store.existsByProductName(orderItemDto.productName())) {
                throw new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage());
            }
        }
    }

    private void validateProductQuantity(List<OrderItemDto> orderItemDtos, Store store) {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            int totalQuantity = store.findTotalQuantityByProductName(orderItemDto.productName());
            if (orderItemDto.quantity() > totalQuantity) {
                throw new IllegalArgumentException(QUANTITY_EXCEEDS_STOCK.getMessage());
            }
        }
    }

}
