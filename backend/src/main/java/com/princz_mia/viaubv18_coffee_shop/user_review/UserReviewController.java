package com.princz_mia.viaubv18_coffee_shop.user_review;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productReviews")
@RequiredArgsConstructor
@Slf4j
public class UserReviewController {

    private final UserReviewService userReviewService;

    @GetMapping()
    public ResponseEntity<List<UserReview>> getReviewsByProductId(@RequestParam(value = "productId", required = true) @NotNull(message = "Product Id must not be null") Long productId) {
        List<UserReview> userReviews = userReviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(userReviews);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<UserReview> addProductReview(@RequestBody @Valid UserReviewRequest userReviewRequest) {
        UserReview userReview = userReviewService.addProductReview(userReviewRequest);
        return ResponseEntity.ok(userReview);
    }
}
