package com.princz_mia.viaubv18_coffee_shop.address;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/create")
    public ResponseEntity<Address> createAddress(@RequestBody @Valid AddressRequest addressRequest) {
        Address address = addressService.createAddress(addressRequest);
        return ResponseEntity.ok(address);
    }

    @PostMapping("/setToUser/{userId}")
    public ResponseEntity<Address> setAddressToUser(
            @PathVariable(value = "userId") Long userId,
            @RequestBody @Valid AddressRequest addressRequest
    ) {
        Address address = addressService.setAddressToUser(userId, addressRequest);
        return ResponseEntity.ok(address);
    }
}
