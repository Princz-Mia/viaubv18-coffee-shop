package com.princz_mia.viaubv18_coffee_shop.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.princz_mia.viaubv18_coffee_shop.address.Address;
import com.princz_mia.viaubv18_coffee_shop.audit.Auditable;
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
public class User extends Auditable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRole role;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer loginAttempts;
    private LocalDateTime lastLogin;
    private boolean isAccountNonLocked;
    private boolean isEnabled;
}
