package com.princz_mia.viaubv18_coffee_shop.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.princz_mia.viaubv18_coffee_shop.address.Address;
import com.princz_mia.viaubv18_coffee_shop.user.role.UserRole;

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
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Table(name = "app_user")
public class User {

    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id", unique = false)
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", unique = false)
    private UserRole role;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private boolean isAccountNonLocked;
    private boolean isEnabled;
}
