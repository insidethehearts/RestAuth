package me.therimuru.RestAuth.utils;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ExpectedResultMatchers {

    public static ResultMatcher[] expectedSignUpResultMatchers() {
        return new ResultMatcher[]{
                jsonPath("$.id").isNumber(),
                jsonPath("$.name").isString(),
                jsonPath("$.username").isString(),
                jsonPath("$.age").isNumber(),
                jsonPath("$.bio").value(nullValue()),
                jsonPath("$.bio_public").value(false)
        };
    }

}