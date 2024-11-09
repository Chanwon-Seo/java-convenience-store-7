package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Store;
import store.dto.OrderItemDto;
import store.parser.CartItemParser;
import store.view.InputView;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;
    private final InputView inputView;
    private final CartItemParser cartItemParser;
    private final PromotionService promotionService;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
        this.cartItemParser = new CartItemParser();
        this.promotionService = new PromotionServiceImpl();
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
        promotionService.setAdditionalProduct(cart, store);
    }
}