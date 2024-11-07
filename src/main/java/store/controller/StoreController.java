package store.controller;

import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Store;
import store.dto.ProductDto;
import store.dto.StoreDto;
import store.dto.StoreInitializationDto;
import store.parser.ProductParser;
import store.parser.PromotionParser;
import store.service.FileReaderService;
import store.service.StoreService;

public class StoreController {

    private final FileReaderService fileReaderService;
    private final PromotionParser promotionParser;
    private final ProductParser productParser;
    private final StoreService storeService;

    public StoreController() {
        this.fileReaderService = new FileReaderService();
        this.productParser = new ProductParser();
        this.promotionParser = new PromotionParser();
        this.storeService = new StoreService();
    }

    public StoreDto initialize() {
        StoreInitializationDto storeInitializationDto = fileReaderService.initializeStoreData();
        return parse(storeInitializationDto);
    }

    public StoreDto parse(StoreInitializationDto storeInitializationDto) {
        List<Promotion> promotions = promotionParser.parse(storeInitializationDto.promotionDtos());
        Map<String, List<Product>> parse = productParser.parse(storeInitializationDto.productDtos(), promotions);
        return StoreDto.of(parse, promotions);
    }

    public void run(StoreDto storeDto) {
        storeService.processOrder(storeDto);
    }

}
