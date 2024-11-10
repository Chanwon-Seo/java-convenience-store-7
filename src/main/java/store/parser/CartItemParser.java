package store.parser;

import static store.validation.CartItemValidator.validateOrderItems;

import java.util.ArrayList;
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
        Product product = store.findProductsByProductNameAndPromotion(cartItemDto.productName());
        if (product.isEligibleForStandardPromotion(cartItemDto.quantity())) {
            return createCartItemWithPromotion(cartItemDto, product);
        }
        return createCartItemForInsufficientStock(cartItemDto, store);
    }

    private CartItem createCartItemForInsufficientStock(CartItemDto cartItemDto, Store store) {
        return new CartItem(store.findProductsByName(cartItemDto.productName()),
                cartItemDto.quantity());
    }

    private CartItem createCartItemWithPromotion(CartItemDto cartItemDto, Product product) {
        List<Product> products = new ArrayList<>();
        products.add(product);
        return new CartItem(products, cartItemDto.quantity());
    }

}
