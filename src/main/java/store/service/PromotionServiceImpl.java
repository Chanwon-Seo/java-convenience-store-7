package store.service;

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
            Product product = getProductFromStore(store, cartItem);
            if (!product.isPromotionalProduct()) {
                continue;
            }
            Promotion promotion = product.getPromotionOrElseThrow();
            processPromotion(cartItem, product, promotion);
        }
    }

    private Product getProductFromStore(Store store, CartItem cartItem) {
        return store.findByProductName(cartItem.getProductName()).getFirst();
    }

    private void processPromotion(CartItem cartItem, Product product, Promotion promotion) {
        if (applyEligiblePromotion(cartItem, product, promotion)) {
            addBonusQuantity(cartItem, product, promotion);
            return;
        }

        addBonusQuantity(cartItem, product, promotion);
        verifyStockAvailability(cartItem, product);
    }

    private boolean applyEligiblePromotion(CartItem cartItem, Product product, Promotion promotion) {
        if (product.isEligibleForBonusProduct(cartItem.getQuantity())) {
            offerAdditionalProduct(cartItem, promotion);
            return true;
        }
        return false;
    }

    private void addBonusQuantity(CartItem cartItem, Product product, Promotion promotion) {
        int bonusQuantity = product.calculateBonusQuantity(cartItem.getTotalQuantity(), promotion);
        cartItem.increaseQuantity(bonusQuantity - cartItem.getFreeQuantity());
    }

    private void offerAdditionalProduct(CartItem cartItem, Promotion promotion) {
        outputView.askAdditionalProduct(cartItem.getProduct().getName(), promotion.getGet());
        if (inputView.getYesOrNo()) {
            cartItem.increaseQuantity(promotion.getGet());
        }
    }

    private void verifyStockAvailability(CartItem cartItem, Product product) {
        if (cartItem.getQuantity() > product.getQuantity()) {
            handleInsufficientStock(cartItem, product);
        }
    }

    private void handleInsufficientStock(CartItem cartItem, Product product) {
        int adjustedQuantity = product.calculateQuantityAfterPromotion(cartItem.getQuantity());
        askUserToAdjustQuantity(cartItem, adjustedQuantity);
    }

    private void askUserToAdjustQuantity(CartItem cartItem, int adjustedQuantity) {
        outputView.askForUnmetPromotion(cartItem.getProduct().getName(), adjustedQuantity);
        if (!inputView.getYesOrNo()) {
            cartItem.decreaseQuantity(adjustedQuantity);
        }
    }
}
