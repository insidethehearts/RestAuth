package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.exception.jwt.refresh.BadRefreshTokenException;
import me.therimuru.RestAuth.mapper.UserMapper;
import me.therimuru.RestAuth.object.JwtInformationWrapper;
import me.therimuru.RestAuth.object.JwtRedisKey;
import me.therimuru.RestAuth.object.TokenType;
import me.therimuru.RestAuth.service.JwtService;
import me.therimuru.RestAuth.service.RedisTokenService;
import me.therimuru.RestAuth.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private UserService userService;
    private JwtService jwtService;

    @ResponseBody
    @PostMapping(value = "/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody @Valid UserSignUpDTO signUpDto) {
        final String refreshToken = userService.register(signUpDto);
        return ResponseEntity
                .ok()
                .header(SET_COOKIE, generateRefreshTokenCookie(refreshToken))
                .build();
    }

    @ResponseBody
    @PostMapping(value = "/sign-in", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> signIn(@RequestBody @Valid UserSignInDTO signInDTO) {
        final String refreshToken = userService.regenerateRefreshToken(signInDTO);
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
                .maxAge(jwtService.getRefreshTokenDuration())
                .build()
                .toString();
    }
}