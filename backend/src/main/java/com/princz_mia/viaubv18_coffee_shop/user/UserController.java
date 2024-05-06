package com.princz_mia.viaubv18_coffee_shop.user;

import com.princz_mia.viaubv18_coffee_shop.config.security.LoginRequest;
import com.princz_mia.viaubv18_coffee_shop.shared.Response;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response> verifyUserAccount(@RequestParam("key") String key) {
        userService.verifyAccountKey(key);
        return ResponseEntity.ok().body(getResponse("Account verified.", HttpStatus.OK));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        UserDto userDto = userService.loginUser(loginRequest);
        return ResponseEntity.ok(userDto);
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

    @PostMapping("/change/password")
    public ResponseEntity<UserDto> changeUserPassword(Long userId, @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {
        UserDto userDto = userService.changeUserPassword(userId, passwordChangeRequest);
        return ResponseEntity.ok(userDto);
    }
}
