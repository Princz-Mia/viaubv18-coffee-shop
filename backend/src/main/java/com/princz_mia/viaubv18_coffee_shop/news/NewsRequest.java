package com.princz_mia.viaubv18_coffee_shop.news;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsRequest {
    private Long id;
    @NotEmpty(message = "News Title cannot be empty or null")
    private String title;
    @NotEmpty(message = "News Content cannot be empty or null")
    private String content;
    //@NotEmpty(message = "News Image cannot be empty or null")
    private String newsImage;
}