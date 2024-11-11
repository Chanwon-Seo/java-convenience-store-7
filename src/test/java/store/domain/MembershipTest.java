package store.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MembershipTest {
    @Test
    void 멤버십_생성_테스트() {
        Membership membership = new Membership();

        assertEquals(membership.getMembershipPrice(), 0);
    }

    @Test
    void 멤버십_할인_계산_테스트() {
        Membership membership = new Membership();

        membership.calculateDiscountedPrice(10000);

        assertEquals(membership.getMembershipPrice(), 3000);
    }

    @Test
    void 멤버십_최대_한도_계산_테스트() {
        Membership membership = new Membership();

        membership.calculateDiscountedPrice(50000);

        assertEquals(membership.getMembershipPrice(), 8000);
    }
}