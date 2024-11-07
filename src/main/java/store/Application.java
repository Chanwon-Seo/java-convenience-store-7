package store;

import store.controller.StoreController;
import store.dto.StoreDto;

public class Application {
    public static void main(String[] args) {
        StoreController controller = new StoreController();
        StoreDto storeDto = controller.initialize();
        controller.run(storeDto);
    }
    
}
