package store.parser;

import static store.validation.CartItemValidator.validateOrderItems;

import java.util.List;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Store;
import store.dto.CartItemDto;

public class CartItemParser {

    public List<CartItem> parse(List<CartItemDto> cartItemDtos, Store store) {
        validateOrderItems(cartItemDtos, store);
        return cartItemDtos.stream()
                .map(orderItemDto -> generateCartItemWithPromotion(orderItemDto, store))
                .toList();
    }

    public CartItem generateCartItemWithPromotion(CartItemDto cartItemDto, Store store) {
        List<Product> products = store.findByProductName(cartItemDto.productName());
        return createCartItemWithPromotion(cartItemDto, products.getFirst());
    }

    private CartItem createCartItemWithPromotion(CartItemDto cartItem, Product product) {
        return new CartItem(product, cartItem.quantity());
    }

}
