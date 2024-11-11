package store.dto;

import java.util.List;

public record StoreInitializationDto(
        List<ProductDto> productDtos,
        List<PromotionDto> promotionDtos
) {
}
