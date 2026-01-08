package me.therimuru.RestAuth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.extern.slf4j.Slf4j;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
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
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

        user1 = newRandomUser();
        user2 = newRandomUser();
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
    void expectSuccess_signUpTest() throws Exception {
        MvcResult signUpResult = signUp(
                user1,
                status().isOk(),
                jsonPath("$.id").isNumber(),
                jsonPath("$.name").isString(),
                
        );
        System.out.println(signUpResult.getResponse().getContentAsString());
        final String user1RefreshToken = signUpResult
                .getResponse()
                .getCookie("R-TOKEN").getValue();
        final String user2RefreshToken = signUp(user2, result -> status().isOk())
                .getResponse()
                .getCookie("R-TOKEN").getValue();
    }

    @Order(2)
    @Test
    void expect409() throws Exception {
        signUp(user1, result -> status().isConflict());
    }

    MvcResult signUp(UserSignUpDTO userSignUpDTO, ResultMatcher... resultMatchers) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userSignUpDTO))
                )
                .andExpectAll(resultMatchers)
                .andReturn();
    }

}