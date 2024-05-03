package com.princz_mia.viaubv18_coffee_shop.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequest {
    @NotEmpty(message = "Country name cannot be null or empty")
    private String countryName;
    @NotEmpty(message = "Region cannot be null or empty")
    private String region;
    @NotEmpty(message = "Postal code cannot be null or empty")
    private String postalCode;
    @NotEmpty(message = "City cannot be null or empty")
    private String city;
    private String addressLine1;
    private String addressLine2;
    @NotEmpty(message = "Street number cannot be null or empty")
    private String streetNumber;
    private String unitNumber;
}
