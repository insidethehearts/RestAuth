package me.therimuru.RestAuth.service.contract.internal;

import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;

public interface UserService {

    UserEntity register(UserSignUpDTO userSignUpDTO) throws UserAlreadyRegisteredException;

    UserEntity findByUsernameAndPassword(String username, String rawPassword) throws UserNotFoundInDatabaseException, InvalidPasswordException;

    UserEntity findBySignInDTO(UserSignInDTO signInDTO) throws UserNotFoundInDatabaseException, InvalidPasswordException;

}