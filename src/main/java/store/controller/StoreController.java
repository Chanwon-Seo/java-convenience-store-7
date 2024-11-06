package store.controller;

import store.dto.StoreInitializationDto;
import store.service.FileReaderService;

public class StoreController {

    private final FileReaderService fileReaderService;

    public StoreController() {
        this.fileReaderService = new FileReaderService();
    }

    public void initialize() {
        StoreInitializationDto storeInitializationDto = fileReaderService.initializeData();
    }

}
