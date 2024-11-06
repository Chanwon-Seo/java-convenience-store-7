package store.parser;

import static store.exception.ParserException.validateDataEmpty;
import static store.exception.ParserException.validatePromotionHeader;

import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;
import store.dto.PromotionDto;

public class PromotionParser {

    public List<Promotion> parse(List<String> data) {
        validate(data);
        return convertToPromotionList(data);
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

    private void validate(List<String> data) {
        validateDataEmpty(data);
        validatePromotionHeader(data);
    }

    private PromotionDto toPromotionDto(String data) {
        return PromotionDto.toPromotionDto(data);
    }
}
