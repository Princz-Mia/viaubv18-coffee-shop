package com.princz_mia.viaubv18_coffee_shop.user_review;

import com.princz_mia.viaubv18_coffee_shop.product.Product;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    Optional<List<UserReview>> findAllByOrderedProduct(Product product);
    Optional<List<UserReview>> findAllByUser(User user);
    Optional<UserReview> findByUserAndOrderedProduct(User userById, Product productById);
}
