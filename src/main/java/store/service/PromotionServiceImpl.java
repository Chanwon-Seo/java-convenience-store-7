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

    /**
     * 장바구니에 있는 모든 상품에 대해 프로모션을 처리하고 보너스 수량을 추가
     */
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

    /**
     * 프로모션 처리 메서드
     */
    private void processPromotion(CartItem cartItem, Product product, Promotion promotion) {
        if (applyEligiblePromotion(cartItem, product, promotion)) {
            addBonusQuantity(cartItem, product, promotion);
            return;
        }

        addBonusQuantity(cartItem, product, promotion);
        verifyStockAvailability(cartItem, product);
    }

    /**
     * 프로모션에 적합한지 확인하고 적합하면 보너스 상품을 제공
     */
    private boolean applyEligiblePromotion(CartItem cartItem, Product product, Promotion promotion) {
        if (product.isEligibleForBonusProduct(cartItem.getQuantity())) {
            offerAdditionalProduct(cartItem, promotion);
            return true;
        }
        return false;
    }

    /**
     * 보너스 수량 추가
     */
    private void addBonusQuantity(CartItem cartItem, Product product, Promotion promotion) {
        int bonusQuantity = product.calculateBonusQuantity(cartItem.getTotalQuantity(), promotion);
        cartItem.increaseQuantity(bonusQuantity - cartItem.getFreeQuantity());
    }

    /**
     * 추가 상품을 제공할지 여부를 사용자에게 묻고, 동의하면 추가
     */
    private void offerAdditionalProduct(CartItem cartItem, Promotion promotion) {
        outputView.askAdditionalProduct(cartItem.getProduct().getName(), promotion.getGet());
        if (inputView.getYesOrNo()) {
            cartItem.increaseQuantity(promotion.getGet());
        }
    }

    /**
     * 장바구니 상품 수량이 재고보다 많으면 재고를 확인
     */
    private void verifyStockAvailability(CartItem cartItem, Product product) {
        if (cartItem.getQuantity() > product.getQuantity()) {
            handleInsufficientStock(cartItem, product);
        }
    }

    /**
     * 재고 부족 시 수량 조정
     */
    private void handleInsufficientStock(CartItem cartItem, Product product) {
        int adjustedQuantity = product.calculateQuantityAfterPromotion(cartItem.getQuantity());
        askUserToAdjustQuantity(cartItem, adjustedQuantity);
    }

    /**
     * 사용자가 재고 부족으로 수량을 조정할 수 있도록 묻고, 동의하지 않으면 정가 수량만큼 감소
     */
    private void askUserToAdjustQuantity(CartItem cartItem, int adjustedQuantity) {
        outputView.askForUnmetPromotion(cartItem.getProduct().getName(), adjustedQuantity);
        if (!inputView.getYesOrNo()) {
            cartItem.decreaseQuantity(adjustedQuantity);
        }
    }
}
