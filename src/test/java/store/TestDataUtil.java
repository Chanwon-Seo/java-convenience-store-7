package store;

import store.controller.FileReaderController;
import store.domain.Store;
import store.dto.StoreDto;

public class TestDataUtil {

    public static Store createStore() {
        StoreDto storeDto = new FileReaderController().runFileData();
        return new Store(storeDto);
    }
}
