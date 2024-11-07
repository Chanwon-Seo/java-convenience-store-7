package store.parser;

import static store.message.ErrorMessage.EMPTY_DATA;

import java.util.List;
import store.domain.Promotion;
import store.dto.PromotionDto;

public class PromotionParser {

    public List<Promotion> parse(List<PromotionDto> promotions) {
        validate(promotions);
        return convertToPromotionList(promotions);
    }

    public List<Promotion> convertToPromotionList(List<PromotionDto> data) {
        return data.stream()
                .map(Promotion::new).toList();
    }

    public void validate(List<PromotionDto> promotions) {
        validateDataEmpty(promotions);
    }

    public void validateDataEmpty(List<PromotionDto> promotions) {
        if (promotions.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DATA.getMessage());
        }
    }

}
