package com.princz_mia.viaubv18_coffee_shop.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a " +
            "WHERE a.country.id = ?1" +
            " and LOWER(a.region) = LOWER(?2)" +
            " and LOWER(a.postalCode) = LOWER(?3)" +
            " and LOWER(a.city) = LOWER(?4)" +
            " and LOWER(a.addressLine1) = LOWER(?5)" +
            " and LOWER(a.addressLine2) = LOWER(?6)" +
            " and LOWER(a.streetNumber) = LOWER(?7)" +
            " and LOWER(a.unitNumber) = LOWER(?8)"
    )
    Optional<Address> findByPropertiesIgnoreCase(Long countryId, String region, String postalCode, String city, String addressLine1, String addressLine2, String streetNumber, String unitNumber);

}
