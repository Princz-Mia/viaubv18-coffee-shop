package com.princz_mia.viaubv18_coffee_shop.order;

import com.princz_mia.viaubv18_coffee_shop.address.Address;
import com.princz_mia.viaubv18_coffee_shop.audit.Auditable;
import com.princz_mia.viaubv18_coffee_shop.order.status.OrderStatus;
import com.princz_mia.viaubv18_coffee_shop.shipping_method.ShippingMethod;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopOrder extends Auditable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_method_id", referencedColumnName = "id")
    private ShippingMethod shippingMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_status_id", referencedColumnName = "id")
    private OrderStatus orderStatus;

    private Timestamp orderDate;
    private double orderTotal;
}
