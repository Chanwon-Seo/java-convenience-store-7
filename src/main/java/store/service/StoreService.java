package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Membership;
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
    private final MembershipService membershipService;
    private final ReceiptService receiptService;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
        this.cartItemParser = new CartItemParser();
        this.promotionService = new PromotionServiceImpl();
        this.orderService = new OrderServiceImpl();
        this.membershipService = new MembershipServiceImpl();
        this.receiptService = new ReceiptServiceImpl();
    }

    /**
     * 주문을 처리하는 메서드로 일련의 작업을 처리
     */
    public void processOrder(Store store) {
        List<Product> products = store.findAll();
        outputView.showStoreOverview(products);
        Cart cart = setOrderItem(store);
        cart = setAdditionalProduct(cart, store);
        Order order = createOrder(cart, store);
        Membership membership = setMemberShip(order);
        updateStoreInventory(store, order);
        setReceipt(store, order, cart, membership);
        askForNextAction(store);
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

    /**
     * 프로모션에 따라 추가 상품을 장바구니에 추가하고, 처리 실패 시 다시 시도
     * <p>
     * 추가로 정가 구매 가격을 차감하여 장바구니의 수량이 1이하라면 상품 선택부터 시작
     */
    private Cart setAdditionalProduct(Cart cart, Store store) {
        boolean success = false;
        while (!success) {
            try {
                promotionService.setFreeProductQuantity(cart, store);
                success = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                cart = setOrderItem(store);
            }
        }
        return cart;
    }

    /**
     * 장바구니에 있는 상품을 바탕으로 주문을 생성
     */
    private Order createOrder(Cart cart, Store store) {
        return orderService.totalOrder(cart, store);
    }

    /**
     * 주문에 대해 멤버십 할인 여부를 확인하고, 해당 할인 적용
     */
    private Membership setMemberShip(Order order) {
        Membership membership = new Membership();
        if (checkMembershipDiscount(order)) {
            return membershipService.setMembership(order, membership);
        }
        return membership;
    }

    /**
     * 주문에 멤버십 할인을 적용할지 여부를 확인
     */
    private boolean checkMembershipDiscount(Order order) {
        if (!order.isNoNonPromotionItems()) {
            outputView.askForMembershipDiscount();
            return inputView.getYesOrNo();
        }
        return false;
    }

    /**
     * 영수증을 출력하는 메서드
     */
    private void setReceipt(Store store, Order order, Cart cart, Membership membership) {
        receiptService.displayReceipt(store, order, cart, membership);
    }

    /**
     * 주문에 따른 재고를 업데이트하는 메서드
     */
    public void updateStoreInventory(Store store, Order order) {
        store.decreaseStockForOrder(order);
    }

    /**
     * 사용자가 주문 후 계속 진행할지 여부를 묻고, 다시 주문을 처리할지 결정
     */
    public void askForNextAction(Store store) {
        outputView.displayThankYouMessageForPurchase();
        if (inputView.getYesOrNo()) {
            processOrder(store);
        }
    }
}