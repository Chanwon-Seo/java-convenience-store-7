package store.service;

import store.domain.Cart;
import store.domain.Store;

public interface PromotionService {
    void setFreeProductQuantity(Cart cart, Store store);
}
