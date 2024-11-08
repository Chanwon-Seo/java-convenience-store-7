package store.parser;

import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;
import static store.message.ErrorMessage.QUANTITY_EXCEEDS_STOCK;

import java.util.List;
import store.domain.OrderItem;
import store.domain.Store;
import store.dto.OrderItemDto;

public class OrderItemParser {

    public List<OrderItem> parse(List<OrderItemDto> orderItemDtos, Store store) {
        validator(orderItemDtos, store);
        return orderItemDtos.stream()
                .map(orderitemDto -> new OrderItem(orderitemDto.productName(), orderitemDto.quantity()))
                .toList();
    }

    public void validator(List<OrderItemDto> orderItemDtos, Store products) {
        validateProductExistence(orderItemDtos, products);
        validateProductQuantity(orderItemDtos, products);
    }

    public void validateProductExistence(List<OrderItemDto> orderItemDtos, Store store) {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            if (!store.existsByProductName(orderItemDto.productName())) {
                throw new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage());
            }
        }
    }

    public void validateProductQuantity(List<OrderItemDto> orderItemDtos, Store store) {
        for (OrderItemDto orderItemDto : orderItemDtos) {
            int totalQuantity = store.findTotalQuantityByProductName(orderItemDto.productName());
            if (orderItemDto.quantity() > totalQuantity) {
                throw new IllegalArgumentException(QUANTITY_EXCEEDS_STOCK.getMessage());
            }
        }
    }

}
