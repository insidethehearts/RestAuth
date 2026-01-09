package me.therimuru.RestAuth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.response.AccessTokenResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestSamples {

    @SneakyThrows
    public static MvcResult signUpResult(MockMvc mockMvc, ObjectMapper objectMapper, UserSignUpDTO userSignUpDTO, ResultMatcher... resultMatchers) {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userSignUpDTO))
                )
                .andExpectAll(resultMatchers)
                .andReturn();
    }

    @SneakyThrows
    public static MvcResult signUpResult(MockMvc mockMvc, ObjectMapper objectMapper, UserSignUpDTO userSignUpDTO) {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userSignUpDTO))
                )
                .andExpectAll(ExpectedResultMatchers.expectedSignUpResultMatchers())
                .andReturn();
    }

    @SneakyThrows
    public static MvcResult signUpResult(MockMvc mockMvc, ObjectMapper objectMapper) {
        return signUpResult(mockMvc, objectMapper, UserUtils.newRandomUser());
    }

    @SneakyThrows
    public static String getAccessToken(MockMvc mockMvc, ObjectMapper objectMapper, String refreshToken) {
        return objectMapper.readValue(mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/auth/get-access-token")
                                .header("R-TOKEN", refreshToken)
                )
                .andReturn().getResponse().getContentAsString(), AccessTokenResponseDTO.class).getAccessToken();
    }

    @SneakyThrows
    public static void subscribe(MockMvc mockMvc, String accessToken, Long targetId) {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/subscriptions/subscribe")
                        .header("Authorization", accessToken)
                        .content("{\"target_id\":%d}".formatted(targetId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(
                status().isOk()
        ).andReturn();
    }
}
