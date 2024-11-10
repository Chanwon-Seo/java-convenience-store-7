package store.domain;

import static store.constants.MembershipConstants.DISCOUNT_RATE;

public class Membership {
    private int membershipPrice;

    public Membership() {
        this.membershipPrice = 0;
    }

    public void calculateDiscountedPrice(int totalPrice) {
        this.membershipPrice = (int) (totalPrice * DISCOUNT_RATE);
    }

    public int getMembershipPrice() {
        return membershipPrice;
    }
}
