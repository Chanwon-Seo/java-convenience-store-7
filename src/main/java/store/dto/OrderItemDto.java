package store.dto;

public record OrderItemDto(
        String productName,
        int quantity
) {
}
