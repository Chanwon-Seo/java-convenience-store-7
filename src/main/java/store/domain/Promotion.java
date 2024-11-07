package store.domain;

import java.time.LocalDateTime;
import store.dto.PromotionDto;
import store.exception.PromotionException;
import store.util.DateParser;

public class Promotion {

    private String name;
    private int buy;
    private int get;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Promotion() {
    }

    public Promotion(PromotionDto promotionDto) {
        PromotionException.validate(promotionDto);
        this.name = promotionDto.name();
        this.buy = Integer.parseInt(promotionDto.buy());
        this.get = Integer.parseInt(promotionDto.get());
        this.startDate = DateParser.dateParse(promotionDto.startDate());
        this.endDate = DateParser.dateParse(promotionDto.endDate());
    }

    public boolean isPromotionName(String productPromotionName) {
        return this.name.equals(productPromotionName);
    }

    public String getName() {
        return name;
    }
}
