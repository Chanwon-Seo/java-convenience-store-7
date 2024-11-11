package store.domain;

import java.time.LocalDateTime;
import store.dto.PromotionDto;
import store.util.DateUtil;
import store.validation.PromotionValidator;

public class Promotion {

    private String name; //프로모션명
    private int buy; // 구매조건
    private int get; //증정수량
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

    public int getTotalRequiredQuantity() {
        return buy + get;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
