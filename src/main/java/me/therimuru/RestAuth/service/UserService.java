package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.dto.UserSignUpDTO;

public interface UserService {

    boolean isRegistrable(UserSignUpDTO userSignUpDTO);

    void register(UserSignUpDTO userSignUpDTO);

}