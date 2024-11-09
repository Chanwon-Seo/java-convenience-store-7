package store.controller;

import store.domain.Cart;
import store.domain.Store;
import store.service.StoreService;

public class StoreController {

    private final StoreService storeService;

    public StoreController() {
        this.storeService = new StoreService();
    }

    public void run(Store store) {
        Cart cart = storeService.processOrder(store);
    }
}
