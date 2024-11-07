package store.controller;

import store.dto.StoreDto;
import store.dto.StoreInitializationDto;
import store.service.FileReaderService;
import store.service.ParserService;

public class StoreController {

    private final FileReaderService fileReaderService;
    private final ParserService parserService;

    public StoreController() {
        this.fileReaderService = new FileReaderService();
        this.parserService = new ParserService();

    }

    public StoreDto initialize() {
        StoreInitializationDto storeInitializationDto = fileReaderService.initializeData();
        return parse(storeInitializationDto);
    }

    public StoreDto parse(StoreInitializationDto storeInitializationDto) {
        return parserService.parseStoreInitialization(storeInitializationDto);
    }

}
