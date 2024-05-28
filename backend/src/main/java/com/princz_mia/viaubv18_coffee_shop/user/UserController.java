package com.princz_mia.viaubv18_coffee_shop.user;

import com.princz_mia.viaubv18_coffee_shop.config.security.LoginRequest;
import com.princz_mia.viaubv18_coffee_shop.shared.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static com.princz_mia.viaubv18_coffee_shop.shared.RequestUtils.getResponse;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        userService.createUser(registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getEmail(), registrationRequest.getPassword());
        return ResponseEntity.created(getUri("/register")).body(getResponse("Account created. Check your email to enable your account.", HttpStatus.CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyUserAccount(@RequestParam("key") @NotEmpty(message = "Key cannot be empty or null") String key) {
        userService.verifyAccountKey(key);
        return ResponseEntity.ok().body(getResponse("Account verified.", HttpStatus.OK));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        UserDto userDto = userService.loginUser(loginRequest);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/requestPasswordReset")
    public ResponseEntity<Response> userRequestedPasswordReset(
            @RequestParam @NotEmpty(message = "Email cannot be empty or null")
            @Email(message = "Invalid email address") String email
    ) {
        userService.userRequestedPasswordReset(email);
        return ResponseEntity.ok().body(getResponse("Password reset details sent to your email", HttpStatus.OK));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Response> resetUserPassword(
            @RequestParam("key") @NotEmpty(message = "Key cannot be empty or null") String key,
            @RequestBody @Valid PasswordResetRequest passwordResetRequest
    ) {
        userService.resetUserPassword(key, passwordResetRequest);
        return ResponseEntity.ok().body(getResponse("Password is successfully changed.", HttpStatus.OK));
    }

    private URI getUri(String path) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user" + path).toUriString());
    }

    @PostMapping("/change/name")
    public ResponseEntity<UserDto> changeUserNames(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String firstName = (String) requestBody.get("firstName");
        String lastName = (String) requestBody.get("lastName");

        UserDto userDto = userService.changeUserNames(userId, firstName, lastName);
        return ResponseEntity.ok(userDto);
    }


    @PostMapping("/change/email")
    public ResponseEntity<UserDto> changeUserEmail(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String email = (String) requestBody.get("email");

        UserDto userDto = userService.changeUserEmail(userId, email);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/change/phoneNumber")
    public ResponseEntity<UserDto> changeUserPhoneNumber(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String phoneNumber = (String) requestBody.get("phoneNumber");

        UserDto userDto = userService.changeUserPhoneNumber(userId, phoneNumber);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/change/password/{userId}")
    public ResponseEntity<UserDto> changeUserPassword(
            @PathVariable(value = "userId") @NotNull(message = "User Id must not be null") Long userId,
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest
    ) {
        UserDto userDto = userService.changeUserPassword(userId, passwordChangeRequest);
        return ResponseEntity.ok(userDto);
    }
}
