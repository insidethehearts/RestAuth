package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
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
        log.info("[AuthC/SignUp] Received sign-up request with valid data. [{}]", signUpDto.debugString());
        final String refreshToken = authService.signUp(signUpDto);
        log.info("[AuthC/SignUp] Returning refresh token as cookie. [{}]", refreshToken);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, generateRefreshTokenCookie(refreshToken))
                .build();
    }

    @ResponseBody
    @PostMapping(value = "/sign-in", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> signIn(@RequestBody @Valid UserSignInDTO signInDTO) {
        log.info("[AuthC/SignIn] Received sign-in request with valid data. [{}]", signInDTO.debugString());
        final String refreshToken = authService.signIn(signInDTO);
        log.info("[AuthC/SignIn] Returning refresh token as cookie. [{}]", refreshToken);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, generateRefreshTokenCookie(refreshToken))
                .build();
    }

    @ResponseBody
    @GetMapping(value = "/get-access-token", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> getAccessToken(@NotNull @RequestHeader("R-TOKEN") String refreshToken) {
        log.info("[AuthC/GetAccessToken] Received access token request with notnull refresh token. [{}]", refreshToken);
        final String accessToken = authService.getAccessToken(refreshToken);
        final Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("access_token", accessToken);
        log.info("[AuthC/GetAccessToken] Returning access token. [{}]", accessToken);
        return ResponseEntity.ok(responseBody);
    };

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