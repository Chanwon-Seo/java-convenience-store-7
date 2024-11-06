package store.service;

import java.util.List;
import store.domain.Promotion;
import store.dto.StoreInitializationDto;
import store.parser.PromotionParser;

public class ParserService {
    private final PromotionParser promotionParser;

    public ParserService() {
        this.promotionParser = new PromotionParser();
    }

    public void parseStoreInitialization(StoreInitializationDto data) {
        List<Promotion> promotions = parsePromotion(data.promotions());
    }

    public List<Promotion> parsePromotion(List<String> promotions) {
        return promotionParser.parse(promotions);
    }


}