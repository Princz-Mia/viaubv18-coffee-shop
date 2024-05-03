package com.princz_mia.viaubv18_coffee_shop.user;

import com.princz_mia.viaubv18_coffee_shop.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String createdAt;
    private Address address;
    private String role;
    private String authorities;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer loginAttempts;
    private String lastLogin;
    private boolean isAccountNonLocked;
    private boolean isEnabled;
    private String token;
}
