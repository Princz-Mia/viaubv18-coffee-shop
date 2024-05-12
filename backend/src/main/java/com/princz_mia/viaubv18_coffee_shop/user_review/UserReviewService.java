package com.princz_mia.viaubv18_coffee_shop.user_review;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.product.Product;
import com.princz_mia.viaubv18_coffee_shop.product.ProductRepository;
import com.princz_mia.viaubv18_coffee_shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<UserReview> getReviewsByProductId(Long productId) {
        var productById = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product is not found with matching Id", HttpStatus.BAD_REQUEST));

        return userReviewRepository.findAllByOrderedProduct(productById)
                .orElseThrow(() -> new AppException("No Review is found with matching Product", HttpStatus.NOT_FOUND));
    }

    public UserReview addProductReview(UserReviewRequest userReviewRequest) {
        var userById = userRepository.findById(userReviewRequest.getUserId())
                .orElseThrow(() -> new AppException("User is not found with matching Id", HttpStatus.BAD_REQUEST));

        var productById = productRepository.findById(userReviewRequest.getProductId())
                .orElseThrow(() -> new AppException("Product is not found with matching Id", HttpStatus.BAD_REQUEST));

        Optional<UserReview> optionalUserReview = userReviewRepository.findByUserAndOrderedProduct(userById, productById);
        if (optionalUserReview.isPresent())
            throw new AppException("User already has a review of this product", HttpStatus.BAD_REQUEST);

        List<Product> productsThatUserOrderedBefore = productRepository.findProductsOrderedByUser(userById);
        if (!productsThatUserOrderedBefore.contains(productById))
            throw new AppException("The user has not ordered this product yet, so cannot write a review for it.", HttpStatus.BAD_REQUEST);

        UserReview userReview = UserReview.builder()
                .user(userById)
                .orderedProduct(productById)
                .ratingValue(userReviewRequest.getRatingValue())
                .comment(userReviewRequest.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        return userReviewRepository.save(userReview);
    }
}
