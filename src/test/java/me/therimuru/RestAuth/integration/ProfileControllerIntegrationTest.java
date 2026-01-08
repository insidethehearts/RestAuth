package me.therimuru.RestAuth.integration;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import jakarta.servlet.http.Cookie;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.response.AccessTokenResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;
    private static Faker faker;
    private static UserSignUpDTO userSignUpDTO1, userSignUpDTO2;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        faker = new Faker();

        userSignUpDTO1 = newRandomUser();
        userSignUpDTO2 = newRandomUser();
    }

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.2.2").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("REDIS_PORT", () -> redis.getMappedPort(6379));
    }

    static UserSignUpDTO newRandomUser() {
        final Name fakerName = faker.name();

        final String name = fakerName.firstName();
        final String surname = fakerName.lastName();
        final int age = faker.number().numberBetween(18, 99);
        final String username = fakerName.username();
        final String password = faker.internet().password(12, 32, true, true, true);

        return new UserSignUpDTO(name, surname, username, age, password);
    }

    @Order(1)
    @Test
    void test() throws Exception {
        final MvcResult signUpResult1 = signUp(userSignUpDTO1, status().isOk());
        final MvcResult signUpResult2 = signUp(userSignUpDTO2, status().isOk());

        final String refreshToken1 = signUpResult1.getResponse().getCookie("R-TOKEN").getValue();
        final String refreshToken2 = signUpResult2.getResponse().getCookie("R-TOKEN").getValue();

        final Long id1 = Long.parseLong(String.valueOf(objectMapper.readValue(signUpResult1.getResponse().getContentAsString(), Map.class).get("id")));
        final Long id2 = Long.parseLong(String.valueOf(objectMapper.readValue(signUpResult2.getResponse().getContentAsString(), Map.class).get("id")));

        final String accessToken1 = objectMapper.readValue(getAccessToken(refreshToken1).getResponse().getContentAsString(), AccessTokenResponseDTO.class).getAccessToken();
        final String accessToken2 = objectMapper.readValue(getAccessToken(refreshToken2).getResponse().getContentAsString(), AccessTokenResponseDTO.class).getAccessToken();

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/profile/edit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"bio\":\"Example bio info\",\"bio_public\":false}")
                    .header("Authorization", accessToken1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"target_id\":%d}".formatted(id2))
                        .header("Authorization", accessToken1)
        ).andExpect(status().isOk());

        System.out.println(mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/profile/%d".formatted(id1))
                        .header("Authorization", accessToken2)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());

    }

    MvcResult signUp(UserSignUpDTO userSignUpDTO, ResultMatcher resultMatcher) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userSignUpDTO))
                )
                .andExpect(resultMatcher)
                .andReturn();
    }

    MvcResult getAccessToken(String refreshToken) throws Exception {
        return mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/auth/get-access-token")
                                .header("R-TOKEN", refreshToken)
                )
                .andReturn();
    }
}
