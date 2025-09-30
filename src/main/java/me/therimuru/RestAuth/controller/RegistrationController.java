package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.mapper.UserMapper;
import me.therimuru.RestAuth.object.ApiResponse;
import me.therimuru.RestAuth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping(value = "/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid UserSignUpDTO userSignUpDTO) {
        if (userService.isRegistrable(userSignUpDTO)) {
            final UserEntity createdUser = userService.register(userSignUpDTO);
            return ApiResponse
                    .builder(HttpStatus.OK)
                    .field("id", createdUser.getId())
                    .field("user", createdUser)
                    .build();
        } else {
            return ApiResponse
                    .builder(HttpStatus.BAD_REQUEST)
                    .field("reason", "User with similar username already exists.")
                    .build();
        }
    }

}