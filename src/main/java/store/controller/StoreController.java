package store.controller;

import store.domain.Store;
import store.dto.StoreDto;
import store.service.StoreService;

public class StoreController {

    private final StoreService storeService;

    public StoreController() {
        this.storeService = new StoreService();
    }

    public void run(StoreDto storeDto) {
        Store store = createStore(storeDto);
        storeService.processOrder(store);
    }

    private Store createStore(StoreDto storeDto) {
        return new Store(storeDto);
    }
}
