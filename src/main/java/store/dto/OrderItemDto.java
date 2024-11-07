package store.dto;

public record OrderItemDto(
        String productName,
        int quantity
) {
    public static OrderItemDto toOrderItemDto(String productName, int quantity) {
        return new OrderItemDto(productName, quantity);
    }
}
