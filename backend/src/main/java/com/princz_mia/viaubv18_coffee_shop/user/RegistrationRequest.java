package com.princz_mia.viaubv18_coffee_shop.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {

    @NotEmpty(message = "Firstname cannot be empty or null")
    private String firstName;
    @NotEmpty(message = "Lastname cannot be empty or null")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Invalid email address")
    private String email;
    @NotEmpty(message = "Password cannot be empty or null")
    private String password;
}
