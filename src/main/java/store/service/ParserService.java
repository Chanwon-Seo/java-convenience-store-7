package store.service;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.StoreInitializationDto;
import store.parser.ProductParser;
import store.parser.PromotionParser;

public class ParserService {
    private final PromotionParser promotionParser;
    private final ProductParser productParser;

    public ParserService() {
        this.productParser = new ProductParser();
        this.promotionParser = new PromotionParser();
    }

    public void parseStoreInitialization(StoreInitializationDto data) {
        List<Promotion> promotions = parsePromotion(data.promotions());
        List<Product> products = parseProduct(data.products(), promotions);
    }

    public List<Promotion> parsePromotion(List<String> promotions) {
        return promotionParser.parse(promotions);
    }

    public List<Product> parseProduct(List<String> products, List<Promotion> promotions) {
        return productParser.parse(products, promotions);
    }

}