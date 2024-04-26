package com.princz_mia.viaubv18_coffee_shop.confirmation;

import com.princz_mia.viaubv18_coffee_shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Optional<Confirmation> findByKey(String key);
    Optional<Confirmation> findByUser(User user);
}
