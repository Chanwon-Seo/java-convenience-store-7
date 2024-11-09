package store.controller;

import store.domain.Store;
import store.service.StoreService;

public class StoreController {

    private final StoreService storeService;

    public StoreController() {
        this.storeService = new StoreService();
    }

    public void run(Store store) {
        storeService.processOrder(store);
    }
}
