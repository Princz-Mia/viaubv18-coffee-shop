package com.princz_mia.viaubv18_coffee_shop.user_review;

import com.princz_mia.viaubv18_coffee_shop.product.Product;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    Optional<UserReview> findByOrderedProduct(Product product);
    Optional<UserReview> findByUser(User user);
}
