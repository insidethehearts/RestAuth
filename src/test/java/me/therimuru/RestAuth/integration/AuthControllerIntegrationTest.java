package me.therimuru.RestAuth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.utils.RequestSamples;
import me.therimuru.RestAuth.utils.UserUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Faker faker;

    private static UserSignUpDTO user1, user2;

    @BeforeAll
    static void setUp() {
        faker = new Faker();

        user1 = UserUtils.newRandomUser();
        user2 = UserUtils.newRandomUser();
    }

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.2.2").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("REDIS_PORT", () -> redis.getMappedPort(6379));
    }


    @Order(1)
    @Test
    void expectSuccess_signUpTest() throws Exception {
        final String user1RefreshToken = RequestSamples.signUpResult(mockMvc, objectMapper, user1)
                .getResponse()
                .getCookie("R-TOKEN").getValue();
        final String user2RefreshToken = RequestSamples.signUpResult(mockMvc, objectMapper, user2)
                .getResponse()
                .getCookie("R-TOKEN").getValue();
    }

    @Order(2)
    @Test
    void expect409() throws Exception {
        RequestSamples.signUpResult(mockMvc, objectMapper, user1, status().isConflict());
    }
}