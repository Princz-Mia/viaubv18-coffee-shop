package com.princz_mia.viaubv18_coffee_shop.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {
    private Long id;
    @NotEmpty(message = "Product Name cannot be empty or null")
    private String name;
    @NotNull(message = "Quantity In Stock cannot be null")
    private Integer qtyInStock;
    @NotNull(message = "Price cannot be null")
    private Double price;
    @NotEmpty(message = "Description cannot be empty or null")
    private String description;
    @NotNull(message = "Category Id cannot be null")
    private Long categoryId;
    //@NotEmpty(message = "Product Image cannot be empty or null")
    private String productImage;
}
