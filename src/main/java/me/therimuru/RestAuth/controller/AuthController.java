package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.dto.requests.auth.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.responses.UserTokenServiceResult;
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
    public ResponseEntity<Object> signUp(@RequestBody @Valid UserSignUpDTO signUpDto) {
        final UserTokenServiceResult result = authService.signUp(signUpDto);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, refreshTokenCookie(result.getRefreshToken()))
                .body(result.getUser());
    }

    @ResponseBody
    @PostMapping(value = "/sign-in", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> signIn(@RequestBody @Valid UserSignInDTO signInDTO) {
        final UserTokenServiceResult result = authService.signIn(signInDTO);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, refreshTokenCookie(result.getRefreshToken()))
                .body(result.getUser());
    }

    @ResponseBody
    @GetMapping(value = "/get-access-token", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> getAccessToken(@NotNull @RequestHeader("R-TOKEN") String refreshToken) {
        final String accessToken = authService.getAccessToken(refreshToken);
        final Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("access_token", accessToken);
        return ResponseEntity.ok(responseBody);
    };

    private String refreshTokenCookie(String refreshToken) {
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