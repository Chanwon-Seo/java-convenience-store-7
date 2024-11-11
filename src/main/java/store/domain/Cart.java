package store.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private final Map<String, CartItem> cartItems;

    public Cart() {
        this.cartItems = new LinkedHashMap<>();
    }

    public void addItem(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            this.cartItems.put(cartItem.getProduct().getName(), cartItem);
        }
    }

    public List<CartItem> getAllItemsInCart() {
        return cartItems.values().stream().toList();
    }

    public int getTotalFreeItemQuantity() {
        return cartItems.values().stream().mapToInt(CartItem::getFreeQuantity).sum();
    }

    public int getTotalFreeItemPrice() {
        return cartItems.values().stream().mapToInt(CartItem::getTotalFreePrice).sum();
    }

    public int getTotalItemPrice() {
        return cartItems.values().stream().mapToInt(CartItem::totalPrice).sum();
    }
}
