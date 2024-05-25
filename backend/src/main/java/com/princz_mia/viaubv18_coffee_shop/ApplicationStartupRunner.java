package com.princz_mia.viaubv18_coffee_shop;

import com.princz_mia.viaubv18_coffee_shop.config.security.PasswordConfiguration;
import com.princz_mia.viaubv18_coffee_shop.credential.Credential;
import com.princz_mia.viaubv18_coffee_shop.credential.CredentialRepository;
import com.princz_mia.viaubv18_coffee_shop.shopping_cart.ShoppingCartService;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import com.princz_mia.viaubv18_coffee_shop.user.UserRepository;
import com.princz_mia.viaubv18_coffee_shop.user.role.Authority;
import com.princz_mia.viaubv18_coffee_shop.user.role.UserRole;
import com.princz_mia.viaubv18_coffee_shop.user.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CredentialRepository credentialRepository;
    private final PasswordConfiguration passwordConfiguration;

    private final ShoppingCartService shoppingCartService;

    @Override
    public void run(String... args) throws Exception {
        initAdminProfile();
    }

    private void initAdminProfile() {
        UserRole adminRole = userRoleRepository.findByNameIgnoreCase(Authority.ADMIN.name());
        User admin = User.builder()
                .address(null)
                .role(adminRole)
                .email("viaubv18.coffee.shop@gmail.com")
                .firstName("ADMIN")
                .lastName("PROFILE")
                .phoneNumber(null)
                .loginAttempts(0)
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .isAccountNonLocked(true)
                .isEnabled(true)
                .build();
        User savedAdmin = userRepository.save(admin);

        String adminPassword = "_TEST_ENVIRONMENT_PASSWORD_";
        String encodedAdminPassword = passwordConfiguration.bCryptPasswordEncoder().encode(adminPassword);

        Credential credential = new Credential(savedAdmin, encodedAdminPassword);
        credentialRepository.save(credential);

        shoppingCartService.createShoppingCartForVerifiedUser(savedAdmin);
    }
}