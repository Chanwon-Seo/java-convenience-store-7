package store.service;

import store.domain.Cart;
import store.domain.Order;
import store.domain.Store;

public interface OrderService {
    Order totalOrder(Cart cart, Store store);
}
