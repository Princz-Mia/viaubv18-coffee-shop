package com.princz_mia.viaubv18_coffee_shop.shop_order.status;

import static com.princz_mia.viaubv18_coffee_shop.shop_order.status.StatusDescription.*;

public enum StatusName {
    PENDING(PENDING_DESCRIPTION),
    AWAITING_PAYMENT(AWAITING_PAYMENT_DESCRIPTION),
    PAYMENT_FULFILLED(PAYMENT_FULFILLED_DESCRIPTION),
    AWAITING_SHIPMENT(AWAITING_SHIPMENT_DESCRIPTION),
    PARTIALLY_SHIPPED(PARTIALLY_SHIPPED_DESCRIPTION),
    SHIPPED(SHIPPED_DESCRIPTION),
    CANCELLED(CANCELLED_DESCRIPTION),
    REFUNDED(REFUNDED_DESCRIPTION);

    private final String value;

    StatusName(String value) { this.value = value; }

    public String getValue() { return this.value; }
}
