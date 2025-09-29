package me.therimuru.RestAuth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.UserSignUpDTO;
import me.therimuru.RestAuth.mapper.UserMapper;
import me.therimuru.RestAuth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserSignUpDTO userSignUpDTO) {
        if (userService.isRegistrable(userSignUpDTO)) {
            userService.register(userSignUpDTO);
            return new ResponseEntity<>("registered", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("user already registered", HttpStatus.BAD_REQUEST);
        }
    }

}