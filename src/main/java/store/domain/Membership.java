package store.domain;

import static store.constants.MembershipConstants.DISCOUNT_RATE;

public class Membership {
    private int membershipPrice;

    public Membership() {
        this.membershipPrice = 0;
    }

    public void calculateDiscountedPrice(int totalPrice) {
        int discount = (int) (totalPrice * DISCOUNT_RATE);
        this.membershipPrice = Math.min(discount, 8000);
    }

    public int getMembershipPrice() {
        return membershipPrice;
    }
}
