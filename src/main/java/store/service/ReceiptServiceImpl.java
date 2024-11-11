package store.service;

import store.domain.Cart;
import store.domain.Membership;
import store.domain.Receipt;
import store.domain.Store;
import store.view.OutputView;

public class ReceiptServiceImpl implements ReceiptService {

    private final OutputView outputView;

    public ReceiptServiceImpl() {
        this.outputView = new OutputView();
    }

    /**
     * 영수증을 출력하는 메서드입니다.
     */
    @Override
    public void displayReceipt(Store store, Cart cart, Membership membership) {
        Receipt receipt = new Receipt(store, cart, membership);
        String ReceiptContent = receipt.toString();
        outputView.displayReceipt(ReceiptContent);
    }

}
