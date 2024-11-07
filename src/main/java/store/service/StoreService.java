package store.service;

import java.util.List;
import store.domain.Product;
import store.dto.StoreDto;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;

    public StoreService() {
        this.outputView = new OutputView();
    }

    public void processOrder(StoreDto storeDto) {
        outputView.showStoreOverview(storeDto.products());
    }

    public void setOrderItem(List<Product> products) {
        outputView.askProductNameAndQuantity();
    }
}
