package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Membership;
import store.domain.Order;
import store.domain.Product;
import store.domain.Receipt;
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
    private final MembershipService membershipService;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
        this.cartItemParser = new CartItemParser();
        this.promotionService = new PromotionServiceImpl();
        this.orderService = new OrderServiceImpl();
        this.membershipService = new MembershipServiceImpl();
    }

    public void processOrder(Store store) {
        List<Product> products = store.findAll();
        outputView.showStoreOverview(products);
        Cart cart = setOrderItem(store);
        setAdditionalProduct(cart, store);
        Order order = createOrder(cart, store);
        Membership membership = setMemberShip(order);
        updateStoreInventory(store, order);
        setReceipt(store, order, membership);
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
        promotionService.setFreeProductQuantity(cart, store);
    }

    private Order createOrder(Cart cart, Store store) {
        return orderService.totalOrder(cart, store);
    }

    private Membership setMemberShip(Order order) {
        Membership membership = new Membership();
        if (checkMembershipDiscount(order)) {
            return membershipService.setMembership(order, membership);
        }
        return membership;

    }

    private boolean checkMembershipDiscount(Order order) {
        if (!order.isNoNonPromotionItems()) {
            outputView.askForMembershipDiscount();
            return inputView.getYesOrNo();
        }
        return false;
    }

    private void setReceipt(Store store, Order order, Membership membership) {
        Receipt receipt = new Receipt(order, membership);
    }

    public void updateStoreInventory(Store store, Order order) {
        store.decreaseStockForOrder(order);
    }
}