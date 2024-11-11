package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Store;
import store.view.InputView;
import store.view.OutputView;

public class PromotionServiceImpl implements PromotionService {
    private final OutputView outputView;
    private final InputView inputView;

    public PromotionServiceImpl() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
    }

    @Override
    public void setFreeProductQuantity(Cart cart, Store store) {
        for (CartItem cartItem : cart.getAllItemsInCart()) {
            Product product = store.findByProductName(cartItem.getProductName()).getFirst();
            if (!product.isPromotionalProduct()) {
                continue;
            }
            Promotion promotion = product.getPromotionOrElseThrow();
            if (applyPromotionIfEligible(cartItem, product, promotion)) {
                continue;
            }
            handleInsufficientStock(cartItem, product);
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
        outputView.askAdditionalProduct(cartItem.getProduct().getName(), promotion.getGet());
        updateOrderItemQuantityIfAccepted(cartItem, promotion.getGet());
    }

    private void updateOrderItemQuantityIfAccepted(CartItem cartItem, int additionalQuantity) {
        if (inputView.getYesOrNo()) {
            cartItem.increaseQuantity(additionalQuantity);
        }
    }

    private void handleInsufficientStock(CartItem cartItem, Product product) {
        if (cartItem.getQuantity() > product.getQuantity()) {
            int totalQuantity = product.calculateQuantityAfterPromotion(cartItem.getQuantity());
            handleUnmetPromotion(cartItem, totalQuantity);
        }
    }

    private void handleUnmetPromotion(CartItem cartItem, int totalQuantity) {
        outputView.askForUnmetPromotion(cartItem.getProduct().getName(), totalQuantity);
        if (!inputView.getYesOrNo()) {
            cartItem.decreaseQuantity(totalQuantity);
        }
    }
}
