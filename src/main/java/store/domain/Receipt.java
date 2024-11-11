package store.domain;

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

public class Receipt {
    private final Store store;
    private final Cart cart;
    private final Membership membership;

    public Receipt(Store store, Cart cart, Membership membership) {
        this.store = store;
        this.cart = cart;
        this.membership = membership;
    }

    @Override
    public String toString() {
        return buildReceiptContent();
    }

    public boolean hasFreeItems() {
        return cart.getTotalFreeItemQuantity() > 0;
    }

    /**
     * 장바구니 및 멤버십 정보를 바탕으로 영수증 내용을 빌드하는 메서드입니다.
     * <p>
     * 추가로 증정된 상품이 없다면 상품내역 정보는 출력하지 않습니다.
     */
    public String buildReceiptContent() {
        StringBuilder result = new StringBuilder();
        result.append(getReceiptHeader());
        result.append(toStringOrderDetail());
        if (hasFreeItems()) {
            result.append(toStringRemainingItemsForPromotion());
        }
        result.append(toStringTotal());
        return result.toString();
    }

    /**
     * 영수증 헤더를 반환하는 메서드입니다.
     */
    public String getReceiptHeader() {
        StringBuilder header = new StringBuilder();
        header.append(RECEIPT_HEADER).append("\n");
        header.append(String.format(PRODUCT_HEADER, PRODUCT_NAME, QUANTITY, PRICE)).append("\n");
        return header.toString();
    }

    /**
     * 주문 내역을 문자열로 반환하는 메서드입니다.
     */
    public String toStringOrderDetail() {
        StringBuilder orderDetail = new StringBuilder();

        for (CartItem cartItem : cart.getAllItemsInCart()) {
            orderDetail.append(String.format(ORDER_DETAIL_FORMAT, cartItem.getProductName(),
                            numberFormat(cartItem.getQuantity()), numberFormat(cartItem.totalPrice())))
                    .append("\n");
        }
        return orderDetail.toString();
    }

    /**
     * 증정 상품 내역을 문자열로 반환하는 메서드입니다.
     */
    public String toStringRemainingItemsForPromotion() {
        StringBuilder remainingitems = new StringBuilder();
        remainingitems.append(PROMOTION_HEADER).append("\n");

        for (CartItem cartItem : cart.getAllItemsInCart()) {
            if (cartItem.getFreeQuantity() > 0) {
                remainingitems.append(String.format(PROMOTION_DETAIL_FORMAT, cartItem.getProductName(),
                                numberFormat(cartItem.getFreeQuantity())))
                        .append("\n");
            }
        }
        return remainingitems.toString();
    }

    /**
     * 총 금액 내역을 문자열로 반환하는 메서드입니다.
     */
    public String toStringTotal() {
        StringBuilder total = new StringBuilder();
        total.append(LINE_SEPARATOR).append("\n");

        total.append(formatTotalItemPrice())
                .append("\n");
        total.append(formatEventDiscount())
                .append("\n");
        total.append(formatMembershipDiscount())
                .append("\n");
        total.append(formatFinalAmount())
                .append("\n");

        return total.toString();
    }

    /**
     * 총 구매 금액을 포맷팅하여 반환
     */
    public String formatTotalItemPrice() {
        return String.format(TOTAL_DETAIL_FORMAT, TOTAL_PURCHASE_AMOUNT, numberFormat(cart.getTotalItemPrice()));
    }

    /**
     * 이벤트 할인 금액을 포맷팅하여 반환
     */
    public String formatEventDiscount() {
        return String.format(TOTAL_DETAIL_FORMAT, EVENT_DISCOUNT, numberFormat(cart.getTotalFreeItemPrice()));
    }

    /**
     * 멤버십 할인 금액을 포맷팅하여 반환
     */
    public String formatMembershipDiscount() {
        return String.format(TOTAL_DETAIL_FORMAT, MEMBERSHIP_DISCOUNT, numberFormat(membership.getMembershipPrice()));
    }

    /**
     * 최종 금액을 포맷팅하여 반환
     */
    public String formatFinalAmount() {
        int finalAmount = cart.getTotalItemPrice() - cart.getTotalFreeItemPrice() - membership.getMembershipPrice();
        return String.format(TOTAL_DETAIL_FORMAT, FINAL_AMOUNT, numberFormat(finalAmount));
    }

    /**
     * 숫자를 한국의 숫자 포맷에 맞게 형식화하는 메서드입니다.
     */
    public String numberFormat(int number) {
        return NumberFormat.getInstance(Locale.KOREA).format(number);
    }
}
