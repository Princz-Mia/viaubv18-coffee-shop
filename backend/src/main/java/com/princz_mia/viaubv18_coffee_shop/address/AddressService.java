package com.princz_mia.viaubv18_coffee_shop.address;

import com.princz_mia.viaubv18_coffee_shop.country.CountryRepository;
import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    public Address createAddress(AddressRequest addressRequest) {
        var country = countryRepository.findByNameIgnoreCase(addressRequest.getCountryName())
                .orElseThrow(() -> new AppException("Country is not found with matching name", HttpStatus.NOT_FOUND));

        var optionalAddress = addressRepository.findByPropertiesIgnoreCase(
                country.getId(),
                addressRequest.getRegion(),
                addressRequest.getPostalCode(),
                addressRequest.getCity(),
                addressRequest.getAddressLine1(),
                addressRequest.getAddressLine2(),
                addressRequest.getStreetNumber(),
                addressRequest.getUnitNumber()
        );

        if (optionalAddress.isPresent())
            return optionalAddress.get();

        Address address = Address.builder()
                .country(country)
                .region(addressRequest.getRegion())
                .postalCode(addressRequest.getPostalCode())
                .city(addressRequest.getCity())
                .addressLine1(addressRequest.getAddressLine1())
                .addressLine2(addressRequest.getAddressLine2())
                .streetNumber(addressRequest.getStreetNumber())
                .unitNumber(addressRequest.getUnitNumber())
                .build();

        return addressRepository.save(address);
    }

    public Address setAddressToUser(Long userId, AddressRequest addressRequest) {
        var country = countryRepository.findByNameIgnoreCase(addressRequest.getCountryName())
                .orElseThrow(() -> new AppException("Country is not found with matching name", HttpStatus.NOT_FOUND));

        var userById = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching Id.", HttpStatus.NOT_FOUND));

        var optionalAddress = addressRepository.findByPropertiesIgnoreCase(
                country.getId(),
                addressRequest.getRegion(),
                addressRequest.getPostalCode(),
                addressRequest.getCity(),
                addressRequest.getAddressLine1(),
                addressRequest.getAddressLine2(),
                addressRequest.getStreetNumber(),
                addressRequest.getUnitNumber()
        );

        if (optionalAddress.isPresent()) {
            Address existingAddress = optionalAddress.get();
            userById.setAddress(existingAddress);
            userRepository.save(userById);
            return existingAddress;
        }

        Address newAddress = Address.builder()
                .country(country)
                .region(addressRequest.getRegion())
                .postalCode(addressRequest.getPostalCode())
                .city(addressRequest.getCity())
                .addressLine1(addressRequest.getAddressLine1())
                .addressLine2(addressRequest.getAddressLine2())
                .streetNumber(addressRequest.getStreetNumber())
                .unitNumber(addressRequest.getUnitNumber())
                .build();

        userById.setAddress(newAddress);
        userRepository.save(userById);
        return addressRepository.save(newAddress);
    }
}
