package com.princz_mia.viaubv18_coffee_shop.product;

import com.princz_mia.viaubv18_coffee_shop.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameIgnoreCase(String name);

    Page<Product> findAllByNameContainsIgnoreCase(String searchTerm, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:searchTerm%")
    List<Product> findByContainingSearchTermInName(@Param("searchTerm") String searchTerm);

    @Query("SELECT DISTINCT oi.product FROM OrderItem oi JOIN oi.order o WHERE o.customer = :user")
    List<Product> findProductsOrderedByUser(@Param("user") User user);
}
