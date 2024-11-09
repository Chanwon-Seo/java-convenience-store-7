package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Store;
import store.dto.OrderItemDto;
import store.parser.CartItemParser;
import store.view.InputView;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;
    private final InputView inputView;
    private final CartItemParser cartItemParser;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
        this.cartItemParser = new CartItemParser();
    }

    public Cart processOrder(Store store) {
        List<Product> products = store.findAll();
        outputView.showStoreOverview(products);
        Cart cart = setOrderItem(store);
        setAdditionalProduct(cart, store);
        return cart;
    }

    private Cart setOrderItem(Store store) {
        outputView.askProductNameAndQuantity();
        List<OrderItemDto> orderItemDtos = inputView.getOrderItem();
        try {
            List<CartItem> cartItems = cartItemParser.parse(orderItemDtos, store);
            return setCart(cartItems);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return setOrderItem(store);
        }
    }

    private Cart setCart(List<CartItem> cartItems) {
        Cart cart = new Cart();
        cartItems.forEach(cart::addItem);
        return cart;
    }

    private void setAdditionalProduct(Cart cart, Store store) {
        for (CartItem cartItem : cart.getOrderItems()) {
            Product product = findProductWithPromotion(store, cartItem.getProductName());
            Promotion promotion = product.getPromotion();
            if (isEligibleForAdditionalProduct(cartItem, product, promotion)) {
                offerAdditionalProduct(cartItem, promotion);
                continue;
            }
            if (cartItem.getQuantity() > product.getQuantity()) {
                int totalQuantity = handleUnmetQuantity(cartItem, product, promotion);
                handleUnmetQuantity(cartItem, totalQuantity);
            }
        }
    }

    private void handleUnmetQuantity(CartItem cartItem, int totalQuantity) {
        outputView.askForUnmetPromotion(cartItem.getProductName(), totalQuantity);
        if (!inputView.getYesOrNo()) {
            cartItem.decreaseQuantity(totalQuantity);
        }
    }

    private int handleUnmetQuantity(CartItem cartItem, Product product, Promotion promotion) {
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int quantityDifference = cartItem.getQuantity() - product.getQuantity();
        int quantityRemainder = product.getQuantity() % requiredQuantity;

        return quantityRemainder + quantityDifference;
    }

    private Product findProductWithPromotion(Store store, String productName) {
        return store.findProductsByProductNameAndPromotion(productName);
    }

    private boolean isEligibleForAdditionalProduct(CartItem cartItem, Product product, Promotion promotion) {
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        return cartItem.getQuantity() % requiredQuantity >= promotion.getBuy()
                && cartItem.getQuantity() + promotion.getGet() <= product.getQuantity();
    }

    private void offerAdditionalProduct(CartItem cartItem, Promotion promotion) {
        outputView.askAdditionalProduct(cartItem.getProductName(), promotion.getGet());
        updateOrderItemQuantityIfAccepted(cartItem, promotion.getGet());
    }

    private void updateOrderItemQuantityIfAccepted(CartItem cartItem, int additionalQuantity) {
        if (inputView.getYesOrNo()) {
            cartItem.increaseQuantity(additionalQuantity);
        }
    }

}