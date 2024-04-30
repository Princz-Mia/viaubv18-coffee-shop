package com.princz_mia.viaubv18_coffee_shop.shopping_cart;

import com.princz_mia.viaubv18_coffee_shop.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @SequenceGenerator(
            name = "shopping_cart_sequence",
            sequenceName = "shopping_cart_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shopping_cart_sequence"
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public ShoppingCart(User user) {
        this.user = user;
    }
}
