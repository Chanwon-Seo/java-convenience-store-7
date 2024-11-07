package store.controller;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
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
        StoreInitializationDto storeInitializationDto = fileReaderService.initializeData();
        return parse(storeInitializationDto);
    }

    public StoreDto parse(StoreInitializationDto storeInitializationDto) {
        List<Promotion> promotions = promotionParser.parse(storeInitializationDto.promotions());
        List<Product> products = productParser.parse(storeInitializationDto.products(), promotions);
        return StoreDto.of(products, promotions);
    }

    public void run(StoreDto storeDto) {
        storeService.processOrder(storeDto);
    }

}
