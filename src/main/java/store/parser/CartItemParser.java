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

    /**
     * 선택한 상품명으로 상품정보를 가져옵니다. 프로모션이 적용된 상품이 있다면 우선적으로 프로모션 값을 주입합니다.
     * <p>
     * 반대로 일반 상품인 경우 일반 상품의 프로모션 정보를 주입받습니다.
     */
    public CartItem generateCartItemWithPromotion(CartItemDto cartItemDto, Store store) {
        List<Product> products = store.findByProductName(cartItemDto.productName());
        return createCartItemWithPromotion(cartItemDto, products.getFirst());
    }

    private CartItem createCartItemWithPromotion(CartItemDto cartItem, Product product) {
        return new CartItem(product, cartItem.quantity());
    }

}
