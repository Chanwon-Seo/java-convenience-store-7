package store.service;

import java.util.List;
import store.domain.Cart;
import store.domain.OrderItem;
import store.domain.Product;
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
        System.out.println();
    }

    public Cart setOrderItem(Store store) {
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

}
