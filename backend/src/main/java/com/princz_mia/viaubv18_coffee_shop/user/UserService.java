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
                .createdAt(LocalDateTime.now())
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

    public void userRequestedPasswordReset(String email) {
        var userByEmail = getUserByEmail(email);

        if (!userByEmail.isEnabled())
            throw new AppException("User profile is not verified yet", HttpStatus.BAD_REQUEST);

        if (!userByEmail.isAccountNonLocked())
            throw new AppException("User profile is currently locked", HttpStatus.BAD_REQUEST);

        Confirmation confirmation = new Confirmation(userByEmail);
        confirmationRepository.save(confirmation);

        publisher.publishEvent(new UserEvent(userByEmail, EventType.RESET_PASSWORD, Map.of("key", confirmation.getKey())));
    }

    public void resetUserPassword(String key, PasswordResetRequest passwordResetRequest) {
        var confirmation = confirmationRepository.findByKey(key)
                .orElseThrow(() -> new AppException("Confirmation is not found with matching key", HttpStatus.BAD_REQUEST));

        String newPassword = passwordResetRequest.getNewPassword();
        String confirmPassword = passwordResetRequest.getConfirmNewPassword();
        if (!newPassword.equals(confirmPassword))
            throw new AppException("New password and confirmation password is not matching", HttpStatus.BAD_REQUEST);

        var user = confirmation.getUser();
        var credential = getCredentialByUserId(user.getId());

        String encodedCurrentPassword = credential.getPassword();
        if (passwordConfiguration.bCryptPasswordEncoder().matches(newPassword, encodedCurrentPassword))
            throw new AppException("New password cannot be the same as the current", HttpStatus.BAD_REQUEST);

        String encodedNewPassword = passwordConfiguration.bCryptPasswordEncoder().encode(newPassword);
        credential.setPassword(encodedNewPassword);
        credentialRepository.save(credential);

        confirmationRepository.delete(confirmation);
    }

    private Confirmation getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key)
                .orElseThrow(() -> new AppException("Confirmation key was not found in database.", HttpStatus.NOT_FOUND));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException("User is not found with matching Email", HttpStatus.NOT_FOUND));
    }

    public UserDto fromUserToUserDto(User user, UserRole role) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setLastLogin(user.getLastLogin().toString());
        userDto.setCreatedAt(user.getCreatedAt().toString());
        userDto.setRole(role.getName());
        userDto.setAuthorities(role.getAuthorities());
        return userDto;
    }

    public Credential getCredentialByUserId(Long userId) {
        return credentialRepository.getCredentialByUserId(userId)
                .orElseThrow(() -> new AppException("Unable to find user credential.", HttpStatus.NOT_FOUND));
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
                .orElseThrow(() -> new AppException("User is not found with matching Email.", HttpStatus.NOT_FOUND));

        var userDto = fromUserToUserDto(userByEmail, userByEmail.getRole());
        var userCredential = getCredentialByUserId(userByEmail.getId()); // TODO: Refactor to credentialService do this task
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

    public UserDto changeUserNames(Long userId, String firstName, String lastName) {
        if (userId == null)
            throw new AppException("User Id must not be null", HttpStatus.BAD_REQUEST);

        if (firstName.isEmpty())
            throw new AppException("Firstname must not be null or empty", HttpStatus.BAD_REQUEST);

        if (lastName.isEmpty())
            throw new AppException("Lastname must not be null or empty", HttpStatus.BAD_REQUEST);

        var userById = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching Id.", HttpStatus.NOT_FOUND));

        if (userById.getFirstName().equals(firstName) && userById.getLastName().equals(lastName))
            throw new AppException("User's name already matching with wanted names", HttpStatus.NOT_MODIFIED);

        userById.setFirstName(firstName);
        userById.setLastName(lastName);

        User updatedUser = userRepository.save(userById);
        return fromUserToUserDto(updatedUser, updatedUser.getRole());
    }

    public UserDto changeUserEmail(Long userId, String email) {
        if (userId == null)
            throw new AppException("User Id must not be null", HttpStatus.BAD_REQUEST);

        if (email.isEmpty())
            throw new AppException("Email must not be null or empty", HttpStatus.BAD_REQUEST);

        var userById = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching Id.", HttpStatus.NOT_FOUND));

        if (userById.getEmail().equals(email))
            throw new AppException("User is already using this email", HttpStatus.NOT_MODIFIED);

        userById.setEmail(email);

        User updatedUser = userRepository.save(userById);
        return fromUserToUserDto(updatedUser, updatedUser.getRole());
    }

    public UserDto changeUserPhoneNumber(Long userId, String phoneNumber) {
        if (userId == null)
            throw new AppException("User Id must not be null", HttpStatus.BAD_REQUEST);

        if (phoneNumber.isEmpty())
            throw new AppException("Phone Number must not be null or empty", HttpStatus.BAD_REQUEST);

        var userById = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching Id.", HttpStatus.NOT_FOUND));

        if (userById.getPhoneNumber().equals(phoneNumber))
            throw new AppException("User is already using this Phone number", HttpStatus.NOT_MODIFIED);

        userById.setPhoneNumber(phoneNumber);

        User updatedUser = userRepository.save(userById);
        return fromUserToUserDto(updatedUser, updatedUser.getRole());
    }

    public UserDto changeUserPassword(Long userId, PasswordChangeRequest passwordChangeRequest) {
        if (userId == null)
            throw new AppException("User Id must not be null", HttpStatus.BAD_REQUEST);

        var userById = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User is not found with matching Id.", HttpStatus.NOT_FOUND));

        var userCredential = getCredentialByUserId(userById.getId());

        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmNewPassword()))
            throw new AppException("New password is not matching with password confirmation", HttpStatus.BAD_REQUEST);

        if (passwordConfiguration.bCryptPasswordEncoder().matches(passwordChangeRequest.getNewPassword(), userCredential.getPassword()))
            throw new AppException("User is already using this password", HttpStatus.BAD_REQUEST);

        String encodedNewPassword = passwordConfiguration.bCryptPasswordEncoder().encode(passwordChangeRequest.getNewPassword());
        userCredential.setPassword(encodedNewPassword);
        credentialRepository.save(userCredential);

        return fromUserToUserDto(userById, userById.getRole());
    }
}
