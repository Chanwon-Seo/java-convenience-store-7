package store.domain;

import java.util.List;
import store.validation.CartItemValidator;

public class CartItem {
    private final List<Product> products;
    private int quantity;

    public CartItem(List<Product> products, int quantity) {
        CartItemValidator.validateCartItem(quantity);
        this.products = products;
        this.quantity = quantity;
    }

    public boolean isProductWithPromotion() {
        return products.size() == 1;
    }

    public Product getProductWithPromotion() {
        return products.getFirst();
    }

    public Product getProductNonPromotion() {
        return products.getFirst();
    }

    public String getProductName() {
        return products.getFirst().getName();
    }

    public void increaseQuantity(int updateQuantity) {
        this.quantity += updateQuantity;
    }

    public void decreaseQuantity(int updateQuantity) {
        this.quantity -= updateQuantity;
    }

    public void removeCartItem() {
        products.removeLast();
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getQuantity() {
        return quantity;
    }

}
