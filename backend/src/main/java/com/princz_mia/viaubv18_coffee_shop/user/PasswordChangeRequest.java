package com.princz_mia.viaubv18_coffee_shop.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordChangeRequest {

    @NotEmpty(message = "Current Password cannot be empty or null")
    private String currentPassword;
    @NotEmpty(message = "New Password cannot be empty or null")
    private String newPassword;
    @NotEmpty(message = "New Password's Confirmation cannot be empty or null")
    private String confirmNewPassword;
}