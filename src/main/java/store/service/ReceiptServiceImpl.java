package store.service;

import static store.constants.ReceiptConstants.EVENT_DISCOUNT;
import static store.constants.ReceiptConstants.FINAL_AMOUNT;
import static store.constants.ReceiptConstants.LINE_SEPARATOR;
import static store.constants.ReceiptConstants.MEMBERSHIP_DISCOUNT;
import static store.constants.ReceiptConstants.ORDER_DETAIL_FORMAT;
import static store.constants.ReceiptConstants.PRICE;
import static store.constants.ReceiptConstants.PRODUCT_HEADER;
import static store.constants.ReceiptConstants.PRODUCT_NAME;
import static store.constants.ReceiptConstants.PROMOTION_DETAIL_FORMAT;
import static store.constants.ReceiptConstants.PROMOTION_HEADER;
import static store.constants.ReceiptConstants.QUANTITY;
import static store.constants.ReceiptConstants.RECEIPT_HEADER;
import static store.constants.ReceiptConstants.TOTAL_DETAIL_FORMAT;
import static store.constants.ReceiptConstants.TOTAL_PURCHASE_AMOUNT;

import java.text.NumberFormat;
import java.util.Locale;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Membership;
import store.domain.Order;
import store.domain.Store;
import store.view.OutputView;

public class ReceiptServiceImpl implements ReceiptService {

    private final OutputView outputView;

    public ReceiptServiceImpl() {
        this.outputView = new OutputView();
    }

    @Override
    public void displayReceipt(Store store, Order order, Cart cart, Membership membership) {
        String ReceiptContent = buildReceiptContent(cart, membership);
        outputView.displayReceipt(ReceiptContent);

    }

    private String buildReceiptContent(Cart cart, Membership membership) {
        StringBuilder result = new StringBuilder();
        result.append(getReceiptHeader());
        result.append(toStringOrderDetail(cart));
        if (cart.getTotalFreeItemQuantity() > 0) {
            result.append(toStringRemainingItemsForPromotion(cart));
        }
        result.append(toStringTotal(cart, membership));
        return result.toString();
    }

    public String getReceiptHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(RECEIPT_HEADER).append("\n");
        sb.append(String.format(PRODUCT_HEADER, PRODUCT_NAME, QUANTITY, PRICE)).append("\n");
        return sb.toString();
    }


    public String toStringOrderDetail(Cart cart) {
        StringBuilder sb = new StringBuilder();

        for (CartItem cartItem : cart.getAllItemsInCart()) {
            sb.append(
                    String.format(ORDER_DETAIL_FORMAT, cartItem.getProductName(), numberFormat(cartItem.getQuantity()),
                            numberFormat(cartItem.totalPrice())));
            sb.append("\n");
        }
        return sb.toString();
    }


    public String toStringRemainingItemsForPromotion(Cart cart) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROMOTION_HEADER).append("\n");

        for (CartItem cartItem : cart.getAllItemsInCart()) {
            if (cartItem.getFreeQuantity() > 0) {
                sb.append(String.format(PROMOTION_DETAIL_FORMAT, cartItem.getProductName(),
                                numberFormat(cartItem.getFreeQuantity())))
                        .append("\n");
            }
        }
        return sb.toString();
    }


    public String toStringTotal(Cart cart, Membership membership) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR).append("\n");

        int finalAmount = cart.getTotalItemPrice() - cart.getTotalFreeItemPrice() - membership.getMembershipPrice();
        sb.append(String.format(TOTAL_DETAIL_FORMAT, TOTAL_PURCHASE_AMOUNT, numberFormat(cart.getTotalItemPrice())))
                .append("\n");
        sb.append(String.format(TOTAL_DETAIL_FORMAT, EVENT_DISCOUNT, numberFormat(cart.getTotalFreeItemPrice())))
                .append("\n");
        sb.append(String.format(TOTAL_DETAIL_FORMAT, MEMBERSHIP_DISCOUNT, numberFormat(membership.getMembershipPrice())))
                .append("\n");
        sb.append(String.format(TOTAL_DETAIL_FORMAT, FINAL_AMOUNT, numberFormat(finalAmount))).append("\n");

        return sb.toString();
    }


    public String numberFormat(int number) {
        return NumberFormat.getInstance(Locale.KOREA).format(number);
    }
}
