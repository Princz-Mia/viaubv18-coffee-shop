package com.princz_mia.viaubv18_coffee_shop.order;

import com.princz_mia.viaubv18_coffee_shop.address.Address;
import com.princz_mia.viaubv18_coffee_shop.order.status.OrderStatus;
import com.princz_mia.viaubv18_coffee_shop.shipping_method.ShippingMethod;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private LocalDateTime orderDate;
    private double orderTotal;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private String paymentId;
}
