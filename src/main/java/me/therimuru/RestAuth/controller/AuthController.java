package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.service.contract.adapter.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;

    @Value("${app.jwt.refresh.duration}")
    private Integer refreshTokenDuration;
    @Value("${app.jwt.refresh.durationUnit}")
    private ChronoUnit refreshTokenDurationUnit;

    @ResponseBody
    @PostMapping(value = "/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody @Valid UserSignUpDTO signUpDto) {
        final String refreshToken = authService.signUp(signUpDto);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, generateRefreshTokenCookie(refreshToken))
                .build();
    }

    @ResponseBody
    @PostMapping(value = "/sign-in", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> signIn(@RequestBody @Valid UserSignInDTO signInDTO) {
        final String refreshToken = authService.signIn(signInDTO);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, generateRefreshTokenCookie(refreshToken))
                .build();
    }

//    @ResponseBody
//    @PostMapping(value = "/get-access-token")
//    public ResponseEntity<Map<String, Object>> getAccessToken(@CookieValue("R-TOKEN") String refreshToken) {
//        final JwtInformationWrapper jwtInformation = jwtService.decodeRefreshToken(refreshToken);
//        final JwtRedisKey redisToken = redisTokenService.getRefreshToken(jwtInformation.id());
//        if (redisToken.token().equals(refreshToken)) {
//            return ResponseEntity.ok().build();
//        } else {
//            throw new BadRefreshTokenException(); // todo
//        }
//    }

    private String generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
                .from("R-TOKEN", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.of(refreshTokenDuration, refreshTokenDurationUnit))
                .build()
                .toString();
    }
}