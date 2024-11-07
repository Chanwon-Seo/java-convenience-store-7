package store.service;

import java.util.List;
import store.domain.OrderItem;
import store.domain.Product;
import store.dto.StoreDto;
import store.parser.OrderItemParser;
import store.view.InputView;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;
    private final InputView inputView;

    public StoreService() {
        this.outputView = new OutputView();
        this.inputView = new InputView();
    }

    public void processOrder(StoreDto storeDto) {
        outputView.showStoreOverview(storeDto.products());
        List<OrderItem> orderItems = setOrderItem(storeDto.products());

    }

    public List<OrderItem> setOrderItem(List<Product> products) {
        outputView.askProductNameAndQuantity();
        String inputData = inputView.getInputData();
        return OrderItemParser.parse(inputData, products);
    }

}
