package store;

import store.controller.FileReaderController;
import store.controller.StoreController;
import store.domain.Store;
import store.dto.StoreDto;

public class FrontController {
    private final FileReaderController fileReaderController;
    private final StoreController storeController;

    public FrontController() {
        this.fileReaderController = new FileReaderController();
        this.storeController = new StoreController();
    }

    public void run() {
        StoreDto storeDto = fileReaderController.runFileData();
        storeController.run(storeDto);
    }
}
