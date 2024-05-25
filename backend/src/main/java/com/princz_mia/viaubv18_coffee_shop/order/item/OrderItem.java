package com.princz_mia.viaubv18_coffee_shop.order.item;

import com.princz_mia.viaubv18_coffee_shop.product.Product;
import com.princz_mia.viaubv18_coffee_shop.order.ShopOrder;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private ShopOrder order;

    private Integer qty;
    private Double price;
}
