package me.therimuru.RestAuth;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import me.therimuru.RestAuth.object.JWTInformationWrapper;
import me.therimuru.RestAuth.service.implementation.JwtServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith({MockitoExtension.class, TestStatusLogger.class})
@Slf4j
public class RestAuthTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private static Faker faker;

    @BeforeAll
    public static void init() {
        faker = new Faker();
        assertNotNull(faker, "Faker instance can't be null.");
    }

    @Test
    @DisplayName("JWT Service Testing: Generating and decoding JWT")
    public void testJwtService(TestInfo testInfo) {
        final JWTInformationWrapper startInformation = new JWTInformationWrapper(faker.number().randomNumber(), faker.name().username().toLowerCase());

        final String token = jwtService.generateAccessJWT(startInformation);
        assertNotNull(token, "JWT can't be null.");

        final JWTInformationWrapper decodedJwtInformation = jwtService.decodeAccessJWT(token);
        assertNotNull(decodedJwtInformation, "Decoded information from JWT can't be null.");

        assertEquals(startInformation, startInformation, "JWT's data must be equals before and after generating.");
    }

}