package com.princz_mia.viaubv18_coffee_shop.order.status;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {

    @SequenceGenerator(
            name = "order_status_sequence",
            sequenceName = "order_status_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_status_sequence"
    )
    private Long id;

    private String name;
}
