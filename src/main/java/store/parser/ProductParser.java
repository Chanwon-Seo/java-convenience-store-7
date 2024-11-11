package store.parser;

import static store.constants.PromotionConstants.NO_PROMOTION_SUFFIX;
import static store.constants.PromotionConstants.PROMOTION_SUFFIX;
import static store.message.ErrorMessage.EMPTY_DATA;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;
import store.util.PromotionUtil;

public class ProductParser {

    private final PromotionUtil promotionUtil;

    public ProductParser() {
        this.promotionUtil = new PromotionUtil();
    }

    public Map<String, Product> parse(List<ProductDto> productDtos, List<Promotion> availablePromotions) {
        validateProductDtos(productDtos);
        return groupAndConvertProductDtos(productDtos, availablePromotions);
    }

    private Map<String, Product> groupAndConvertProductDtos(List<ProductDto> productDtos,
                                                            List<Promotion> availablePromotions) {
        Map<String, List<ProductDto>> groupedProductDtos = groupProductDtosByProductName(productDtos);
        applyDefaultPromotionForSingleProduct(groupedProductDtos);
        return convertGroupedProductDtosToProducts(groupedProductDtos, availablePromotions);
    }

    /**
     * 주어진 그룹화된 제품 DTO 목록을 기반으로, 프로모션을 고려하여 제품 맵을 생성합니다.
     */
    private Map<String, Product> convertGroupedProductDtosToProducts(Map<String, List<ProductDto>> groupedProductDtos,
                                                                     List<Promotion> availablePromotions) {
        Map<String, Product> productMap = new LinkedHashMap<>();
        for (Entry<String, List<ProductDto>> entry : groupedProductDtos.entrySet()) {
            for (ProductDto productDto : entry.getValue()) {
                if (addProductWithoutPromotion(availablePromotions, entry, productDto, productMap)) {
                    continue;
                }
                addProductWithPromotion(availablePromotions, entry, productDto, productMap);
            }
        }
        return productMap;
    }

    /**
     * 주어진 프로모션을 적용하여 제품을 생성한 후, 제품 맵에 추가합니다.
     */
    private void addProductWithPromotion(List<Promotion> availablePromotions, Entry<String, List<ProductDto>> entry,
                                         ProductDto productDto, Map<String, Product> productMap) {
        productMap.put(entry.getKey() + PROMOTION_SUFFIX,
                createProductFromDto(productDto, availablePromotions));
    }

    /**
     * 프로모션이 없는 제품을 제품 맵에 추가합니다. 제품 DTO의 프로모션 필드가 null인 경우에만 호출됩니다.
     */
    private boolean addProductWithoutPromotion(List<Promotion> availablePromotions,
                                               Entry<String, List<ProductDto>> entry,
                                               ProductDto productDto, Map<String, Product> productMap) {
        if (productDto.promotion() == null) {
            productMap.put(entry.getKey() + NO_PROMOTION_SUFFIX,
                    createProductFromDto(productDto, availablePromotions));
            return true;
        }
        return false;
    }

    /**
     * 주어진 제품 DTO와 사용 가능한 프로모션 목록을 기반으로 Product 객체를 생성합니다.
     *
     * @param productDto          변환할 제품 DTO
     * @param availablePromotions 사용 가능한 프로모션 목록
     * @return 생성된 Product 객체
     */
    private Product createProductFromDto(ProductDto productDto, List<Promotion> availablePromotions) {
        Promotion promotion = promotionUtil.findMatchingPromotion(productDto.promotion(), availablePromotions);
        return new Product(productDto, Optional.ofNullable(promotion));
    }

    /**
     * 그룹화된 제품 DTO 목록에서 프로모션이 있는 단일 제품에 기본 프로모션을 적용합니다.
     *
     * @param groupedProductDtos 제품 이름을 기준으로 그룹화된 제품 DTO 맵
     */
    private void applyDefaultPromotionForSingleProduct(Map<String, List<ProductDto>> groupedProductDtos) {
        for (List<ProductDto> productDtos : groupedProductDtos.values()) {
            if (isSingleProductWithPromotion(productDtos)) {
                productDtos.add(ProductDto.toGeneralProductDto(productDtos.getFirst()));
            }
        }
    }

    /**
     * 주어진 제품 DTO 리스트가 단일 제품이고, 해당 제품에 프로모션이 있는지 확인합니다.
     */
    private boolean isSingleProductWithPromotion(List<ProductDto> productDtos) {
        return productDtos.size() == 1 && productDtos.getFirst().promotion() != null;
    }

    /**
     * 제품 DTO 목록을 제품 이름을 기준으로 그룹화하여 반환합니다.
     */
    private Map<String, List<ProductDto>> groupProductDtosByProductName(List<ProductDto> productDtos) {
        Map<String, List<ProductDto>> groupedProductDtos = new LinkedHashMap<>();
        for (ProductDto productDto : productDtos) {
            groupedProductDtos.computeIfAbsent(productDto.name(), k -> new ArrayList<>())
                    .add(productDto);
        }
        return groupedProductDtos;
    }

    private void validateProductDtos(List<ProductDto> productDtos) {
        if (productDtos.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DATA.getMessage());
        }
    }

}
