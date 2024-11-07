package store.controller;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.StoreDto;
import store.dto.StoreInitializationDto;
import store.parser.ProductParser;
import store.parser.PromotionParser;
import store.service.FileReaderService;

public class StoreController {

    private final FileReaderService fileReaderService;
    private final PromotionParser promotionParser;
    private final ProductParser productParser;

    public StoreController() {
        this.fileReaderService = new FileReaderService();
        this.productParser = new ProductParser();
        this.promotionParser = new PromotionParser();
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

}
