package store.parser;

import static store.message.ErrorMessage.EMPTY_DATA;
import static store.message.ErrorMessage.NOT_FOUND_PROMOTION;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;

public class ProductParser {

    public Map<String, List<Product>> parse(List<ProductDto> productDtos, List<Promotion> availablePromotions) {
        validate(productDtos);
        return convertToProductsMap(productDtos, availablePromotions);
    }

    public Map<String, List<Product>> convertToProductsMap(List<ProductDto> productDtos,
                                                           List<Promotion> availablePromotions) {
        Map<String, List<ProductDto>> groupedProductDtos = groupProductDtosByName(productDtos);
        applyDefaultPromotionIfMissing(groupedProductDtos);
        return convertToProductEntities(groupedProductDtos, availablePromotions);
    }

    public Map<String, List<Product>> convertToProductEntities(Map<String, List<ProductDto>> groupedProductDtos,
                                                               List<Promotion> availablePromotions) {
        Map<String, List<Product>> productEntities = new LinkedHashMap<>();

        for (Map.Entry<String, List<ProductDto>> entry : groupedProductDtos.entrySet()) {
            String productName = entry.getKey();
            List<ProductDto> productDtos = entry.getValue();

            List<Product> products = convertToProductList(productDtos, availablePromotions);
            productEntities.put(productName, products);
        }

        return productEntities;
    }

    private List<Product> convertToProductList(List<ProductDto> productDtos, List<Promotion> availablePromotions) {
        List<Product> products = new ArrayList<>();

        for (ProductDto productDto : productDtos) {
            Product product = createProductFromDto(productDto, availablePromotions);
            products.add(product);
        }

        return products;
    }

    public Product createProductFromDto(ProductDto productDto, List<Promotion> availablePromotions) {
        Promotion promotion = findMatchingPromotion(productDto.promotion(), availablePromotions);
        return new Product(productDto, promotion);
    }

    public Promotion findMatchingPromotion(String promotionName, List<Promotion> availablePromotions) {
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

    public void applyDefaultPromotionIfMissing(Map<String, List<ProductDto>> groupedProductDtos) {
        for (List<ProductDto> productDtos : groupedProductDtos.values()) {
            if (isSingleProductWithPromotion(productDtos)) {
                productDtos.add(
                        ProductDto.toGeneralProductDto(productDtos.getFirst()));
            }
        }
    }

    public boolean isSingleProductWithPromotion(List<ProductDto> productDtos) {
        return productDtos.size() < 2 && productDtos.getFirst().promotion() != null;
    }

    private Map<String, List<ProductDto>> groupProductDtosByName(List<ProductDto> productDtos) {
        Map<String, List<ProductDto>> groupedProductDtos = new LinkedHashMap<>();
        for (ProductDto productDto : productDtos) {
            groupedProductDtos.computeIfAbsent(productDto.name(), k -> new ArrayList<>())
                    .add(productDto);
        }
        return groupedProductDtos;
    }

    public void validate(List<ProductDto> productDtos) {
        validateDataNotEmpty(productDtos);
    }

    public void validateDataNotEmpty(List<ProductDto> productDtos) {
        if (productDtos.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DATA.getMessage());
        }
    }
}
