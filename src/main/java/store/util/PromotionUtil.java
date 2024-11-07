package store.util;

import static store.message.ErrorMessage.NOT_FOUND_PROMOTION;

import java.util.List;
import store.domain.Promotion;

public class PromotionUtil {

    public Promotion findMatchingPromotion(String promotionName, List<Promotion> availablePromotions) {
        if (promotionName == null) {
            return null;
        }
        return findPromotionByName(availablePromotions, promotionName);
    }

    public Promotion findPromotionByName(List<Promotion> availablePromotions, String promotionName) {
        for (Promotion promotion : availablePromotions) {
            if (promotion.isPromotionName(promotionName)) {
                return promotion;
            }
        }
        throw new IllegalArgumentException(NOT_FOUND_PROMOTION.getMessage());
    }
}
