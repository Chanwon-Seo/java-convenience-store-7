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
        String ReceiptContent = buildReceiptContent(cart, membership);
        outputView.displayReceipt(ReceiptContent);

    }

    /**
     * 장바구니 및 멤버십 정보를 바탕으로 영수증 내용을 빌드하는 메서드입니다.
     */
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

    /**
     * 영수증 헤더를 반환하는 메서드입니다.
     */
    public String getReceiptHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(RECEIPT_HEADER).append("\n");
        sb.append(String.format(PRODUCT_HEADER, PRODUCT_NAME, QUANTITY, PRICE)).append("\n");
        return sb.toString();
    }

    /**
     * 주문 내역을 문자열로 반환하는 메서드입니다.
     */
    public String toStringOrderDetail(Cart cart) {
        StringBuilder sb = new StringBuilder();

        for (CartItem cartItem : cart.getAllItemsInCart()) {
            sb.append(String.format(ORDER_DETAIL_FORMAT, cartItem.getProductName(),
                            numberFormat(cartItem.getQuantity()), numberFormat(cartItem.totalPrice())))
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * 증정 상품 내역을 문자열로 반환하는 메서드입니다.
     */
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

    /**
     * 총 금액 내역을 문자열로 반환하는 메서드입니다.
     */
    public String toStringTotal(Cart cart, Membership membership) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR).append("\n");

        int finalAmount = cart.getTotalItemPrice() - cart.getTotalFreeItemPrice() - membership.getMembershipPrice();
        sb.append(String.format(TOTAL_DETAIL_FORMAT, TOTAL_PURCHASE_AMOUNT, numberFormat(cart.getTotalItemPrice())))
                .append("\n");
        sb.append(String.format(TOTAL_DETAIL_FORMAT, EVENT_DISCOUNT, numberFormat(cart.getTotalFreeItemPrice())))
                .append("\n");
        sb.append(
                        String.format(TOTAL_DETAIL_FORMAT, MEMBERSHIP_DISCOUNT, numberFormat(membership.getMembershipPrice())))
                .append("\n");
        sb.append(String.format(TOTAL_DETAIL_FORMAT, FINAL_AMOUNT, numberFormat(finalAmount))).append("\n");

        return sb.toString();
    }

    /**
     * 숫자를 한국의 숫자 포맷에 맞게 형식화하는 메서드입니다.
     */
    public String numberFormat(int number) {
        return NumberFormat.getInstance(Locale.KOREA).format(number);
    }
}
