package store.domain;

import java.time.LocalDateTime;
import store.dto.PromotionDto;
import store.validation.PromotionValidator;
import store.util.DateUtil;

public class Promotion {

    private String name;
    private int buy;
    private int get;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Promotion() {
    }

    public Promotion(PromotionDto promotionDto) {
        PromotionValidator.validate(promotionDto);
        this.name = promotionDto.name();
        this.buy = Integer.parseInt(promotionDto.buy());
        this.get = Integer.parseInt(promotionDto.get());
        this.startDate = DateUtil.dateParse(promotionDto.startDate());
        this.endDate = DateUtil.dateParse(promotionDto.endDate());
    }

    public boolean isPromotionName(String productPromotionName) {
        return this.name.equals(productPromotionName);
    }

    public String getName() {
        return name;
    }
}
