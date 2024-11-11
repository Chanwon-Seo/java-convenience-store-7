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

    private void addProductWithPromotion(List<Promotion> availablePromotions, Entry<String, List<ProductDto>> entry,
                                         ProductDto productDto, Map<String, Product> productMap) {
        productMap.put(entry.getKey() + PROMOTION_SUFFIX,
                createProductFromDto(productDto, availablePromotions));
    }

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

    private Product createProductFromDto(ProductDto productDto, List<Promotion> availablePromotions) {
        Promotion promotion = promotionUtil.findMatchingPromotion(productDto.promotion(), availablePromotions);
        return new Product(productDto, Optional.ofNullable(promotion));
    }

    private void applyDefaultPromotionForSingleProduct(Map<String, List<ProductDto>> groupedProductDtos) {
        for (List<ProductDto> productDtos : groupedProductDtos.values()) {
            if (isSingleProductWithPromotion(productDtos)) {
                productDtos.add(ProductDto.toGeneralProductDto(productDtos.getFirst()));
            }
        }
    }

    private boolean isSingleProductWithPromotion(List<ProductDto> productDtos) {
        return productDtos.size() == 1 && productDtos.getFirst().promotion() != null;
    }

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
