package me.therimuru.RestAuth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.javafaker.Faker;
import me.therimuru.RestAuth.exception.jwt.access.BadAccessJWTException;
import me.therimuru.RestAuth.exception.jwt.access.ExpiredAccessJWTException;
import me.therimuru.RestAuth.object.JwtInformationWrapper;
import me.therimuru.RestAuth.object.TokenType;
import me.therimuru.RestAuth.service.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith({MockitoExtension.class, TestStatusLogger.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    private static Faker faker;

    @BeforeAll
    public static void init() {
        faker = new Faker();
        assertNotNull(faker, "Faker instance can't be null.");
    }

    @Test
    @DisplayName("JWT Service Testing: Generating and decoding JWT")
    @Order(1)
    public void jwtCycleTest() {
        final JwtInformationWrapper startInformation = new JwtInformationWrapper(
                faker.number().randomNumber(),
                faker.name().username().toLowerCase(),
                TokenType.ACCESS
        );

        final String token = jwtService.generateAccessToken(startInformation);
        assertNotNull(token, "JWT can't be null.");

        final JwtInformationWrapper decodedJwtInformation = jwtService.decodeAccessToken(token);
        assertNotNull(decodedJwtInformation, "Decoded information from JWT can't be null.");

        assertEquals(startInformation, startInformation, "JWT's data must be equals before and after generating.");
    }

    @Test
    @DisplayName("JWT Service Testing: Decoding expired JWT")
    @Order(2)
    public void decodeJWT_expired() {
        final JwtInformationWrapper jwtInformation = new JwtInformationWrapper(
                faker.number().randomNumber(),
                faker.name().username().toLowerCase(),
                TokenType.ACCESS,
                Instant.now(),
                Instant.now().minus(10, ChronoUnit.SECONDS)
        );

        final String token = jwtService.generateAccessToken(jwtInformation);
        assertThrows(ExpiredAccessJWTException.class, () -> jwtService.decodeAccessToken(token));
    }

    @Test
    @DisplayName("JWT Service Testing: Bad JWT algorithm")
    @Order(3)
    public void decodeJWT_badAlgorithm() throws NoSuchFieldException, IllegalAccessException {
        final JwtInformationWrapper jwtInformation = new JwtInformationWrapper(faker.number().randomNumber(), faker.name().username(), TokenType.ACCESS);
        final String token = jwtService.generateAccessToken(jwtInformation);

        final Field jwtVerifier = jwtService.getClass().getDeclaredField("jwtVerifier");
        jwtVerifier.setAccessible(true);
        jwtVerifier.set(
                jwtService,
                JWT.require(Algorithm.HMAC384("BAD_SECRET")).build()
        );

        assertThrows(BadAccessJWTException.class, () -> jwtService.decodeAccessToken(token));
    }

}