package store.service;

import store.domain.Cart;
import store.domain.Order;

public interface OrderService {
    Order totalOrder(Cart cart);
}
