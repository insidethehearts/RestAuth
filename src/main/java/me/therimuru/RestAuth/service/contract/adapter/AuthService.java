package me.therimuru.RestAuth.service.contract.adapter;

import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;

public interface AuthService {

    /**
     *
     * @param userSignUpDTO validated DTO from controller
     * @return Refresh Token as {@link String}
     * @throws UserAlreadyRegisteredException when user with provided username already registered
     */
    String signUp(UserSignUpDTO userSignUpDTO);

    /**
     *
     * @param userSignInDTO validated DTO from controller
     * @return Refresh token as {@link String}
     * @throws UserNotFoundInDatabaseException when entry with provided username not found
     * @throws InvalidPasswordException when entry with provided username was found, but password invalid
     */
    String signIn(UserSignInDTO userSignInDTO);

    String getAccessToken(String refreshToken);
}