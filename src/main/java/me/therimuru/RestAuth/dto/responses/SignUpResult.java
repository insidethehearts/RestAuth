package me.therimuru.RestAuth.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.therimuru.RestAuth.entity.UserEntity;

@AllArgsConstructor
@Getter
public class SignUpResult {

    private UserEntity user;

    private String refreshToken;

}