package store.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private final Map<String, List<CartItem>> cartItems;

    public Cart() {
        this.cartItems = new LinkedHashMap<>();
    }

    public void addItem(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            this.cartItems.computeIfAbsent(cartItem.getProductName(), k -> new ArrayList<>())
                    .add(cartItem);
        }
    }

    public List<CartItem> getAllItemsInCart() {
        List<CartItem> allCartItems = new ArrayList<>();
        for (List<CartItem> productList : cartItems.values()) {
            allCartItems.addAll(productList);
        }
        return allCartItems;
    }
}
