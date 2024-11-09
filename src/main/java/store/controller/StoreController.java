package store.controller;

import java.util.List;
import java.util.Map;
import store.domain.Cart;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Store;
import store.dto.StoreDto;
import store.dto.StoreInitializationDto;
import store.parser.ProductParser;
import store.parser.PromotionParser;
import store.service.FileReaderService;
import store.service.ReceiptService;
import store.service.StoreService;

public class StoreController {

    private final FileReaderService fileReaderService;
    private final PromotionParser promotionParser;
    private final ProductParser productParser;
    private final StoreService storeService;
    private final ReceiptService receiptService;

    public StoreController() {
        this.fileReaderService = new FileReaderService();
        this.productParser = new ProductParser();
        this.promotionParser = new PromotionParser();
        this.storeService = new StoreService();
        this.receiptService = new ReceiptService();
    }

    public void run() {
        StoreDto storeDto = initialize();
        Store store = createStore(storeDto);
        Cart cart = storeService.processOrder(store);
        receiptService.run(store, cart);
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

    private Store createStore(StoreDto storeDto) {
        return new Store(storeDto);
    }
}
