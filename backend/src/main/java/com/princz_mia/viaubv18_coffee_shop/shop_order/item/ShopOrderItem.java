package com.princz_mia.viaubv18_coffee_shop.shop_order.item;

import com.princz_mia.viaubv18_coffee_shop.product.Product;
import com.princz_mia.viaubv18_coffee_shop.shop_order.ShopOrder;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    private ShopOrder shopOrder;

    private Integer qty;
    private Double price;
}
