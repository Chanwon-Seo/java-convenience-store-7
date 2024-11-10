package store.parser;

import static store.message.ErrorMessage.EMPTY_DATA;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, List<Product>> parse(List<ProductDto> productDtos, List<Promotion> availablePromotions) {
        validate(productDtos);
        return convertToProductsMap(productDtos, availablePromotions);
    }

    private Map<String, List<Product>> convertToProductsMap(List<ProductDto> productDtos,
                                                            List<Promotion> availablePromotions) {
        Map<String, List<ProductDto>> groupedProductDtos = groupProductDtosByName(productDtos);
        applyDefaultPromotionIfMissing(groupedProductDtos);
        return convertToProductEntities(groupedProductDtos, availablePromotions);
    }

    private Map<String, List<Product>> convertToProductEntities(Map<String, List<ProductDto>> groupedProductDtos,
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

    private Product createProductFromDto(ProductDto productDto, List<Promotion> availablePromotions) {
        Promotion promotion = promotionUtil.findMatchingPromotion(productDto.promotion(), availablePromotions);
        return new Product(productDto, Optional.ofNullable(promotion));
    }

    private void applyDefaultPromotionIfMissing(Map<String, List<ProductDto>> groupedProductDtos) {
        for (List<ProductDto> productDtos : groupedProductDtos.values()) {
            if (isSingleProductWithPromotion(productDtos)) {
                productDtos.add(ProductDto.toGeneralProductDto(productDtos.getFirst()));
            }
        }
    }

    private boolean isSingleProductWithPromotion(List<ProductDto> productDtos) {
        return productDtos.size() == 1 && productDtos.getFirst().promotion() != null;
    }

    private Map<String, List<ProductDto>> groupProductDtosByName(List<ProductDto> productDtos) {
        Map<String, List<ProductDto>> groupedProductDtos = new LinkedHashMap<>();
        for (ProductDto productDto : productDtos) {
            groupedProductDtos.computeIfAbsent(productDto.name(), k -> new ArrayList<>())
                    .add(productDto);
        }
        return groupedProductDtos;
    }

    private void validate(List<ProductDto> productDtos) {
        if (productDtos.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_DATA.getMessage());
        }
    }
}
