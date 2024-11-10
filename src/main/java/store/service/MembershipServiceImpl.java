package store.service;

import store.domain.Membership;
import store.domain.Order;

public class MembershipServiceImpl implements MembershipService {

    @Override
    public Membership setMembership(Order order, Membership membership) {
        membership.calculateDiscountedPrice(order.totalPriceNonPromotion());
        return membership;
    }

}
