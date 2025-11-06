package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;

import java.util.Optional;

public interface UserService {

    String register(UserSignUpDTO userSignUpDTO);

    String regenerateRefreshToken(UserSignInDTO userSignInDTO);

//    UserEntity findByUsernameAndPassword(String username, String password) throws UserNotFoundInDatabaseException, InvalidPasswordException;

}