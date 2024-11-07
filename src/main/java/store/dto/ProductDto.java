package store.dto;

public record ProductDto(
        String name,
        String price,
        String quantity,
        String promotion
) {
    private static final String DEFAULT_QUANTITY = "0";

    public static ProductDto toProductDto(String name, String price, String quantity, String promotion) {
        return new ProductDto(name, price, quantity, promotion);
    }

    public static ProductDto toGeneralProductDto(ProductDto productDto) {
        return new ProductDto(productDto.name, productDto.price, DEFAULT_QUANTITY, null);
    }

}
