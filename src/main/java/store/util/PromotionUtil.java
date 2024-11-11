package store.util;

import static store.message.ErrorMessage.NOT_FOUND_PROMOTION;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.List;
import store.domain.Promotion;

public class PromotionUtil {

    /**
     * 주어진 프로모션 이름과 일치하는 프로모션을 목록에서 찾습니다.
     */
    public Promotion findMatchingPromotion(String promotionName, List<Promotion> availablePromotions) {
        if (promotionName == null) {
            return null;
        }
        return findPromotionByName(promotionName, availablePromotions);
    }

    /**
     * 프로모션 이름으로 프로모션을 목록에서 찾습니다. 일치하는 프로모션이 없으면 예외를 발생시킵니다.
     */
    public Promotion findPromotionByName(String promotionName, List<Promotion> availablePromotions) {
        for (Promotion promotion : availablePromotions) {
            if (promotion.isPromotionName(promotionName)) {
                return promotion;
            }
        }
        throw new IllegalArgumentException(NOT_FOUND_PROMOTION.getMessage());
    }

    /**
     * 주어진 프로모션 이름에 해당하는 프로모션의 날짜 유효성을 검증합니다. 현재 시간이 프로모션의 유효 날짜 범위 내에 있는지 확인합니다.
     */
    public boolean date(String promotionName, List<Promotion> availablePromotions) {
        LocalDateTime now = DateTimes.now();

        for (Promotion availablePromotion : availablePromotions) {
            if (availablePromotion.isPromotionName(promotionName)) {
                LocalDateTime startDate = availablePromotion.getStartDate();
                LocalDateTime endDate = availablePromotion.getEndDate();

                return now.isAfter(startDate) && now.isBefore(endDate);
            }
        }
        return false;
    }

}