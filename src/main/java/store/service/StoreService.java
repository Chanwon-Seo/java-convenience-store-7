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

    public void processOrder(Store store) {
        List<Product> products = store.findAll();
        outputView.showStoreOverview(products);
        Cart cart = setOrderItem(store);
        setAdditionalProduct(cart, store);
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

    private Cart setCart(List<CartItem> cartItem) {
        Cart cart = new Cart();
        cart.addItem(cartItem);
        return cart;
    }

    private void setAdditionalProduct(Cart cart, Store store) {
        for (CartItem cartItem : cart.getAllItemsInCart()) {
            Product product = store.findProductsByProductNameAndPromotion(cartItem.getProductName());
            if (!product.isPromotionalProduct()) {
                continue;
            }
            Promotion promotion = product.getPromotion();
            if (applyPromotionIfEligible(cartItem, product, promotion)) {
                continue;
            }
            handleInsufficientStock(cartItem, product, promotion);
        }
    }

    private boolean applyPromotionIfEligible(CartItem cartItem, Product product, Promotion promotion) {
        if (product.isEligibleForBonusProduct(cartItem.getQuantity())) {
            offerAdditionalProduct(cartItem, promotion);
            return true;
        }
        return false;
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

    private void handleInsufficientStock(CartItem cartItem, Product product, Promotion promotion) {
        if (cartItem.getQuantity() > product.getQuantity()) {
            int totalQuantity = handleUnmetQuantity(cartItem, product, promotion);
            handleUnmetQuantity(cartItem, totalQuantity);
        }
    }

    private int handleUnmetQuantity(CartItem cartItem, Product product, Promotion promotion) {
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int quantityDifference = cartItem.getQuantity() - product.getQuantity();
        int quantityRemainder = product.getQuantity() % requiredQuantity;

        return quantityRemainder + quantityDifference;
    }

    private void handleUnmetQuantity(CartItem cartItem, int totalQuantity) {
        outputView.askForUnmetPromotion(cartItem.getProductName(), totalQuantity);
        if (!inputView.getYesOrNo()) {
            cartItem.decreaseQuantity(totalQuantity);
        }
    }

}