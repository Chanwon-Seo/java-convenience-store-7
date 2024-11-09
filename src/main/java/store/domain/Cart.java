package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
    }

    public void addItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public List<CartItem> getOrderItems() {
        return cartItems;
    }
}
