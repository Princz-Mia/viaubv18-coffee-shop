package com.princz_mia.viaubv18_coffee_shop.shopping_cart.item;

import com.princz_mia.viaubv18_coffee_shop.audit.Auditable;
import com.princz_mia.viaubv18_coffee_shop.product.Product;
import com.princz_mia.viaubv18_coffee_shop.shopping_cart.ShoppingCart;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem extends Auditable {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private Integer qty;
}
