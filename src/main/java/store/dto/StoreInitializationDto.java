package store.dto;

import java.util.List;

public record StoreInitializationDto(
        List<ProductDto> productDtos,
        List<PromotionDto> promotionDtos
) {
    public static StoreInitializationDto of(List<ProductDto> productDtos, List<PromotionDto> promotionDtos) {
        return new StoreInitializationDto(productDtos, promotionDtos);
    }

}
