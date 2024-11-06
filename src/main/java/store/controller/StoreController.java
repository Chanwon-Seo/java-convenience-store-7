package store.controller;

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

    public void initialize() {
        StoreInitializationDto storeInitializationDto = fileReaderService.initializeData();
        parse(storeInitializationDto);
    }

    public void parse(StoreInitializationDto storeInitializationDto) {
        parserService.parseStoreInitialization(storeInitializationDto);
    }

}
