package me.therimuru.RestAuth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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

    @BeforeAll
    static void setUp() {
        faker = new Faker();
    }

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.2.2").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("REDIS_PORT", () -> redis.getMappedPort(6379));
    }

    @Test
    void authControllerTest() throws Exception {

        final UserSignUpDTO user1 = newRandomUser();

        final MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user1))
                )
                .andExpect(result1 -> MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    UserSignUpDTO newRandomUser() {
        final Name fakerName = faker.name();

        final String name = fakerName.firstName();
        final String surname = fakerName.lastName();
        final int age = faker.number().numberBetween(18, 99);
        final String username = fakerName.username();
        final String password = faker.internet().password(12, 32, true, true, true);

        return new UserSignUpDTO(name, surname, username, age, password);
    }

}