package store.service;

import store.domain.Membership;
import store.domain.Order;

public interface MembershipService {
    Membership setMembership(Order order, Membership membership);
}
