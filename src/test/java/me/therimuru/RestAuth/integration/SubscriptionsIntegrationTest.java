package me.therimuru.RestAuth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.utils.RequestSamples;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SubscriptionsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Faker faker = new Faker();

    private static final HashMap<Integer, UserSignUpDTO> users = new HashMap<>();
    private static final HashMap<Integer, String> userRefreshTokens = new HashMap<>();
    private static final HashMap<Integer, String> userAccessTokens = new HashMap<>();

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.2.2").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("REDIS_PORT", () -> redis.getMappedPort(6379));
    }

    @SneakyThrows
    @Order(1)
    @Test
    void test() {
        for (int i = 1; i <= 10; i++) {
            final MvcResult signUpResult = RequestSamples.signUpResult(mockMvc, objectMapper);
            final String refreshToken = signUpResult.getResponse().getCookie("R-TOKEN").getValue();
            final String accessToken = RequestSamples.getAccessToken(mockMvc, objectMapper, refreshToken);
            userRefreshTokens.put(i, refreshToken);
            userAccessTokens.put(i, accessToken);
        }

        for (int i = 1; i < 10; i++) {
            final String accessToken = userAccessTokens.get(i);
            RequestSamples.subscribe(mockMvc, accessToken, 10L);
        }

        System.out.println(mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/subscriptions/subscribers")
                        .header("Authorization", userAccessTokens.get(10))
        ).andReturn().getResponse().getContentAsString());
    }
}