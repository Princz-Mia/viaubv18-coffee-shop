package com.princz_mia.viaubv18_coffee_shop.user.role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @SequenceGenerator(
            name = "user_role_sequence",
            sequenceName = "user_role_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_role_sequence"
    )
    private Long id;

    private String name;
    private String authorities;
}
