package store.controller;

import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.StoreDto;
import store.dto.StoreInitializationDto;
import store.parser.ProductParser;
import store.parser.PromotionParser;
import store.service.FileReaderService;

public class FileReaderController {
    private final FileReaderService fileReaderService;
    private final PromotionParser promotionParser;
    private final ProductParser productParser;

    public FileReaderController() {
        this.fileReaderService = new FileReaderService();
        this.productParser = new ProductParser();
        this.promotionParser = new PromotionParser();
    }

    public StoreDto runFileData() {
        return initialize();
    }

    public StoreDto initialize() {
        StoreInitializationDto storeInitializationDto = fileReaderService.initializeStoreData();
        return parseStoreData(storeInitializationDto);
    }

    public StoreDto parseStoreData(StoreInitializationDto storeInitializationDto) {
        List<Promotion> promotions = promotionParser.parse(storeInitializationDto.promotionDtos());
        Map<String, List<Product>> parse = productParser.parse(storeInitializationDto.productDtos(), promotions);
        return new StoreDto(parse, promotions);
    }
}
