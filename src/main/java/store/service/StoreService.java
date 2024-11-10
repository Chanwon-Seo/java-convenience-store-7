package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Order;
import store.domain.Product;
import store.domain.Store;
import store.dto.CartItemDto;
import store.parser.CartItemParser;
import store.view.InputView;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;
    private final InputView inputView;
    private final CartItemParser cartItemParser;
    private final PromotionService promotionService;
    private final OrderService orderService;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
        this.cartItemParser = new CartItemParser();
        this.promotionService = new PromotionServiceImpl();
        this.orderService = new OrderServiceImpl();
    }

    public void processOrder(Store store) {
        List<Product> products = store.findAll();
        outputView.showStoreOverview(products);
        Cart cart = setOrderItem(store);
        setAdditionalProduct(cart, store);
        Order order = totalOrder(cart);
    }

    private Cart setOrderItem(Store store) {
        outputView.askProductNameAndQuantity();
        List<CartItemDto> cartItemDtos = inputView.getOrderItem();
        try {
            List<CartItem> cartItems = cartItemParser.parse(cartItemDtos, store);
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

    private Order totalOrder(Cart cart) {
        return orderService.totalOrder(cart);
    }

}