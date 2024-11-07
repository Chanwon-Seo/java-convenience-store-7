package store.service;

import java.util.List;
import store.domain.Product;
import store.domain.Store;
import store.view.OutputView;

public class StoreService {
    private final OutputView outputView;

    public StoreService() {
        this.outputView = new OutputView();
    }

    public void processOrder(Store store) {
        List<Product> products = store.flattenProducts();
        outputView.showStoreOverview(products);
    }

}
