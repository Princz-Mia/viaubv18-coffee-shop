package com.princz_mia.viaubv18_coffee_shop.credential;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findCredentialByUserId(Long userId);
    Optional<Credential> getCredentialByUserId(Long id);
}
