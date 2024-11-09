package store.parser;

import static store.validation.CartItemValidator.validateOrderItems;

import java.util.ArrayList;
import java.util.List;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Store;
import store.dto.OrderItemDto;

public class CartItemParser {

    public List<CartItem> parse(List<OrderItemDto> orderItemDtos, Store store) {
        validateOrderItems(orderItemDtos, store);
        return orderItemDtos.stream()
                .map(orderItemDto -> generateCartItemWithPromotion(orderItemDto, store))
                .toList();
    }

    public CartItem generateCartItemWithPromotion(OrderItemDto orderItemDto, Store store) {
        Product product = store.findProductsByProductNameAndPromotion(orderItemDto.productName());
        if (product.isEligibleForPromotion(orderItemDto.quantity())) {
            return createCartItemWithPromotion(orderItemDto, product);
        }
        return createCartItemForInsufficientStock(orderItemDto, store);
    }

    private CartItem createCartItemForInsufficientStock(OrderItemDto orderItemDto, Store store) {
        return new CartItem(store.findProductsByName(orderItemDto.productName()),
                orderItemDto.quantity());
    }

    private CartItem createCartItemWithPromotion(OrderItemDto orderItemDto, Product product) {
        List<Product> products = new ArrayList<>();
        products.add(product);
        return new CartItem(products, orderItemDto.quantity());
    }

}
