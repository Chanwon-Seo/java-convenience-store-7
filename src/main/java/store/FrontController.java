package store;

import store.controller.FileReaderController;
import store.controller.StoreController;
import store.domain.Store;

public class FrontController {
    private final FileReaderController fileReaderController;
    private final StoreController storeController;

    public FrontController() {
        this.fileReaderController = new FileReaderController();
        this.storeController = new StoreController();
    }

    public void run() {
        Store store = fileReaderController.runFileData();
        storeController.run(store);
    }
}
