package store.parser;

import static store.exception.ParserException.validateDataEmpty;
import static store.exception.ParserException.validateProductHeader;
import static store.message.ErrorMessage.NOT_FOUND_PROMOTION;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;

public class ProductParser {

    public List<Product> parse(List<String> productRows, List<Promotion> availablePromotions) {
        validate(productRows);
        return convertToProducts(productRows, availablePromotions);
    }

    public List<Product> convertToProducts(List<String> productRows, List<Promotion> availablePromotions) {
        List<ProductDto> productDtos = convertToProductDtos(productRows);
        return getProducts(productDtos, availablePromotions);
    }

    public List<ProductDto> convertToProductDtos(List<String> productRows) {
        Map<String, List<ProductDto>> groupedProductDataMap = groupDataByProductName(productRows);
        addDefaultPromotionIfMissing(groupedProductDataMap);
        return mergeProductDtoLists(groupedProductDataMap);
    }

    public List<Product> getProducts(List<ProductDto> productDtos, List<Promotion> availablePromotions) {
        return productDtos.stream()
                .map(productDto -> createProduct(productDto, availablePromotions))
                .toList();
    }

    public Map<String, List<ProductDto>> groupDataByProductName(List<String> productRows) {
        Map<String, List<ProductDto>> groupedProductDataMap = new LinkedHashMap<>();
        for (int i = 1; i < productRows.size(); i++) {
            ProductDto productDto = ProductDto.toProductDto(productRows.get(i));
            groupedProductDataMap.putIfAbsent(productDto.name(), new ArrayList<>());
            groupedProductDataMap.get(productDto.name()).add(productDto);
        }
        return groupedProductDataMap;
    }

    public List<ProductDto> mergeProductDtoLists(Map<String, List<ProductDto>> groupedProductDataMap) {
        List<ProductDto> allProductDtos = new ArrayList<>();
        for (List<ProductDto> productDtoList : groupedProductDataMap.values()) {
            allProductDtos.addAll(productDtoList);
        }
        return allProductDtos;
    }

    public void addDefaultPromotionIfMissing(Map<String, List<ProductDto>> groupedProductDataMap) {
        for (List<ProductDto> productDtos : groupedProductDataMap.values()) {
            if (isSingleProductWithPromotion(productDtos)) {
                productDtos.add(ProductDto.toGeneralProductDto(productDtos.getFirst()));
            }
        }
    }

    public boolean isSingleProductWithPromotion(List<ProductDto> productDtos) {
        return productDtos.size() < 2 && productDtos.getFirst().promotion() != null;
    }

    public Product createProduct(ProductDto productDto, List<Promotion> availablePromotions) {
        Promotion matchingPromotion = getPromotionOrNull(productDto.promotion(), availablePromotions);
        return new Product(productDto, matchingPromotion);
    }

    public Promotion getPromotionOrNull(String promotionName, List<Promotion> availablePromotions) {
        if (promotionName == null) {
            return null;
        }
        return findPromotionByName(availablePromotions, promotionName);
    }

    public Promotion findPromotionByName(List<Promotion> availablePromotions, String promotionName) {
        for (Promotion promotion : availablePromotions) {
            if (promotion.isPromotionName(promotionName)) {
                return promotion;
            }
        }
        throw new IllegalArgumentException(NOT_FOUND_PROMOTION.getMessage());
    }

    public void validate(List<String> productRows) {
        validateDataEmpty(productRows);
        validateProductHeader(productRows);
    }
}
