package com.princz_mia.viaubv18_coffee_shop.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.user.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final UserRepository userRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto dto) {
        Date now = Date.from(Instant.now());
        Date validity = new Date(now.getTime() + 3600);

        return JWT.create()
                .withIssuer(dto.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName", dto.getFirstName())
                .withClaim("lastName", dto.getLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decoded = verifier.verify(token);

        UserDto userDto = getUserDtoFromDecodedJWT(decoded);

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }

    private UserDto getUserDtoFromDecodedJWT(DecodedJWT decoded) {
        User userByEmail = userRepository.findByEmailIgnoreCase(decoded.getIssuer())
                .orElseThrow(() -> new AppException("User is not found in database.", HttpStatus.NOT_FOUND));

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userByEmail, userDto);
        userDto.setLastLogin(userByEmail.getLastLogin().toString());
        userDto.setCreatedAt(userByEmail.getCreatedAt().toString());
        userDto.setUpdatedAt(userByEmail.getUpdatedAt().toString());
        userDto.setRole(userByEmail.getRole().getName());
        userDto.setAuthorities(userByEmail.getRole().getAuthorities());

        return userDto;
    }
}
