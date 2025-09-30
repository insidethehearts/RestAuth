package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.dto.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;

public interface UserService {

    boolean isRegistrable(UserSignUpDTO userSignUpDTO);

    UserEntity register(UserSignUpDTO userSignUpDTO);

}