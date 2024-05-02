package com.princz_mia.viaubv18_coffee_shop.user;

import com.princz_mia.viaubv18_coffee_shop.cache.CacheStorage;
import com.princz_mia.viaubv18_coffee_shop.config.security.LoginRequest;
import com.princz_mia.viaubv18_coffee_shop.config.security.LoginType;
import com.princz_mia.viaubv18_coffee_shop.config.security.PasswordConfiguration;
import com.princz_mia.viaubv18_coffee_shop.config.security.UserAuthenticationProvider;
import com.princz_mia.viaubv18_coffee_shop.credential.Credential;
import com.princz_mia.viaubv18_coffee_shop.credential.CredentialRepository;
import com.princz_mia.viaubv18_coffee_shop.events.EventType;
import com.princz_mia.viaubv18_coffee_shop.events.UserEvent;
import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.confirmation.Confirmation;
import com.princz_mia.viaubv18_coffee_shop.confirmation.ConfirmationRepository;
import com.princz_mia.viaubv18_coffee_shop.shopping_cart.ShoppingCartService;
import com.princz_mia.viaubv18_coffee_shop.user.role.Authority;
import com.princz_mia.viaubv18_coffee_shop.user.role.UserRole;
import com.princz_mia.viaubv18_coffee_shop.user.role.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ConfirmationRepository confirmationRepository;
    private final CredentialRepository credentialRepository;
    private final ShoppingCartService shoppingCartService;
    private final CacheStorage<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;
    private final PasswordConfiguration passwordConfiguration;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public void createUser(String firstName, String lastName, String email, String password) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        if (userByEmail.isPresent())
            throw new AppException("This email address is already in use", HttpStatus.BAD_REQUEST);

        User savedUser = userRepository.save(createNewUser(firstName, lastName, email));

        String encodedPassword = passwordConfiguration.bCryptPasswordEncoder().encode(password);
        Credential credential = new Credential(savedUser, encodedPassword);
        credentialRepository.save(credential);

        Confirmation confirmation = new Confirmation(savedUser);
        confirmationRepository.save(confirmation);

        publisher.publishEvent(new UserEvent(savedUser, EventType.REGISTRATION, Map.of("key", confirmation.getKey())));
    }

    private User createNewUser(String firstName, String lastName, String email) {
        UserRole role = userRoleRepository.findByNameIgnoreCase(Authority.USER.name());
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .phoneNumber(EMPTY)
                .address(null)
                .loginAttempts(0)
                .lastLogin(LocalDateTime.now())
                .isAccountNonLocked(true)
                .isEnabled(false)
                .build();
    }

    public void verifyAccountKey(String key) {
        Confirmation confirmation = getUserConfirmation(key);
        User user = getUserByEmail(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmation);
        shoppingCartService.createShoppingCartForVerifiedUser(user);
    }

    private Confirmation getUserConfirmation(String key) {
        var confirmationByKey =  confirmationRepository.findByKey(key);
        return confirmationByKey.orElseThrow(() -> new AppException("Confirmation key was not found in database.", HttpStatus.NOT_FOUND));
    }

    private User getUserByEmail(String email) {
        var userByEmail =  userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(() -> new AppException("User was not found with matching email address in database.", HttpStatus.NOT_FOUND));
    }

    private UserDto getUserDtoById(Long id) {
        var userById =  userRepository.findById(id).orElseThrow(() -> new AppException("User is not found in database.", HttpStatus.NOT_FOUND));
        return fromUserToUserDto(userById, userById.getRole(), getCredentialById(userById.getId()));
    }
    private UserDto getUserDtoByEmail(String email) {
        var userByEmail =  userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new AppException("User is not found in database.", HttpStatus.NOT_FOUND));
        return fromUserToUserDto(userByEmail, userByEmail.getRole(), getCredentialById(userByEmail.getId()));
    }

    public UserDto fromUserToUserDto(User user, UserRole role, Credential credential) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setLastLogin(user.getLastLogin().toString());
        userDto.setCreatedAt(user.getCreatedAt().toString());
        //userDto.setUpdatedAt(user.getUpdatedAt().toString());
        userDto.setUpdatedAt("0");
        userDto.setRole(role.getName());
        userDto.setAuthorities(role.getAuthorities());
        return userDto;
    }

    public Credential getCredentialById(Long id) {
        var credentialById = credentialRepository.getCredentialByUserId(id);
        return credentialById.orElseThrow(() -> new AppException("Unable to find user credential.", HttpStatus.NOT_FOUND));
    }

    private void updateLoginAttempt(String email, LoginType loginType) {
        User user = getUserByEmail(email);
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(user.getEmail()) == null) {
                    user.setLoginAttempts(0);
                    user.setAccountNonLocked(true);
                }
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userCache.put(user.getEmail(), user.getLoginAttempts());
                if (userCache.get(user.getEmail()) > 5) {
                    user.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                user.setAccountNonLocked(true);
                user.setLoginAttempts(0);
                user.setLastLogin(LocalDateTime.now());
                userCache.evict(user.getEmail());
            }
        }
        userRepository.save(user);
    }

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if (!userPrincipal.isAccountNonLocked()) { throw new LockedException("Your account is currently locked."); }
        if (!userPrincipal.isEnabled()) { throw new DisabledException("Your account is currently disabled."); }
        if (!userPrincipal.isCredentialsNonExpired()) { throw new CredentialsExpiredException("Your password has expired. Please update your password."); }
        if (!userPrincipal.isAccountNonExpired()) { throw new DisabledException("Your account has expired. Please contact the support."); }
    };

    public UserDto loginUser(LoginRequest loginRequest) {
        User userByEmail = userRepository.findByEmailIgnoreCase(loginRequest.getEmail())
                .orElseThrow(() -> new AppException("User is not found in database.", HttpStatus.NOT_FOUND));

        var userDto = fromUserToUserDto(userByEmail, userByEmail.getRole(), getCredentialById(userByEmail.getId()));
        var userCredential = getCredentialById(userByEmail.getId()); // TODO: Refactor to credentialService do this task
        var userPrincipal = new UserPrincipal(userDto, userCredential);

        try {
            validAccount.accept(userPrincipal);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.FORBIDDEN);
        }

        if (passwordConfiguration.bCryptPasswordEncoder().matches(loginRequest.getPassword(), userPrincipal.getPassword())) {
            updateLoginAttempt(userByEmail.getEmail(), LoginType.LOGIN_SUCCESS);
            userDto.setToken(userAuthenticationProvider.createToken(userDto));
            return userDto;
        } else {
            updateLoginAttempt(userByEmail.getEmail(), LoginType.LOGIN_ATTEMPT);
            throw new AppException("\nInvalid password.\nLogin attempts: " + userByEmail.getLoginAttempts(), HttpStatus.BAD_REQUEST);
        }
    }
}
