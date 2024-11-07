package store.parser;

import static store.message.ErrorMessage.EMPTY_DATA;

import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;
import store.dto.PromotionDto;

public class PromotionParser {

    public List<Promotion> parse(List<String> promotions) {
        validate(promotions);
        return convertToPromotionList(promotions);
    }

    private List<Promotion> convertToPromotionList(List<String> data) {
        List<Promotion> promotions = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            PromotionDto promotionDto = toPromotionDto(data.get(i));
            Promotion promotion = new Promotion(promotionDto);
            promotions.add(promotion);
        }
        return promotions;
    }

    public void validate(List<String> promotions) {
        validateDataEmpty(promotions);
    }

    public void validateDataEmpty(List<String> promotions) {
        if (promotions.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DATA.getMessage());
        }
    }

    private PromotionDto toPromotionDto(String data) {
        return PromotionDto.toPromotionDto(data);
    }
}
