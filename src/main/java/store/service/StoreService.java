package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Store;
import store.dto.OrderItemDto;
import store.parser.OrderItemParser;
import store.view.InputView;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;
    private final InputView inputView;
    private final OrderItemParser orderItemParser;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
        this.orderItemParser = new OrderItemParser();
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
            List<OrderItem> orderItems = orderItemParser.parse(orderItemDtos, store);
            return setCart(orderItems);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return setOrderItem(store);
        }
    }

    private Cart setCart(List<OrderItem> orderItems) {
        Cart cart = new Cart();
        orderItems.forEach(cart::addItem);
        return cart;
    }

    private void setAdditionalProduct(Cart cart, Store store) {
        for (OrderItem orderItem : cart.getOrderItems()) {
            Product product = findProductWithPromotion(store, orderItem.getProductName());
            Promotion promotion = product.getPromotion();
            if (isEligibleForAdditionalProduct(orderItem, product, promotion)) {
                offerAdditionalProduct(orderItem, promotion);
                continue;
            }
            if (orderItem.getQuantity() > product.getQuantity()) {
                int totalQuantity = handleUnmetQuantity(orderItem, product, promotion);
                handleUnmetQuantity(orderItem, totalQuantity);
            }
        }
    }

    private void handleUnmetQuantity(OrderItem orderItem, int totalQuantity) {
        outputView.askForUnmetPromotion(orderItem.getProductName(), totalQuantity);
        if (!inputView.getYesOrNo()) {
            orderItem.decreaseQuantity(totalQuantity);
        }
    }

    private int handleUnmetQuantity(OrderItem orderItem, Product product, Promotion promotion) {
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int quantityDifference = orderItem.getQuantity() - product.getQuantity();
        int quantityRemainder = product.getQuantity() % requiredQuantity;

        return quantityRemainder + quantityDifference;
    }

    private Product findProductWithPromotion(Store store, String productName) {
        return store.findProductsByProductNameAndPromotion(productName);
    }

    private boolean isEligibleForAdditionalProduct(OrderItem orderItem, Product product, Promotion promotion) {
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        return orderItem.getQuantity() % requiredQuantity >= promotion.getBuy()
                && orderItem.getQuantity() + promotion.getGet() <= product.getQuantity();
    }

    private void offerAdditionalProduct(OrderItem orderItem, Promotion promotion) {
        outputView.askAdditionalProduct(orderItem.getProductName(), promotion.getGet());
        updateOrderItemQuantityIfAccepted(orderItem, promotion.getGet());
    }

    private void updateOrderItemQuantityIfAccepted(OrderItem orderItem, int additionalQuantity) {
        if (inputView.getYesOrNo()) {
            orderItem.increaseQuantity(additionalQuantity);
        }
    }

}