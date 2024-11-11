package store.service;

import store.domain.Cart;
import store.domain.Membership;
import store.domain.Order;
import store.domain.Store;

public interface ReceiptService {
    void displayReceipt(Store store, Cart cart, Membership membership);
}
