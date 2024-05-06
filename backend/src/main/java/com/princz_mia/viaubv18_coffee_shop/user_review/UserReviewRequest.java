package com.princz_mia.viaubv18_coffee_shop.user_review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserReviewRequest {

    @NotNull(message = "User Id cannot be null")
    private Long userId;
    @NotNull(message = "Product Id cannot be null")
    private Long productId;
    @NotNull(message = "Rating Value cannot be null")
    private Double ratingValue;
    @NotEmpty(message = "Comment cannot be empty or null")
    private String comment;
}