package me.therimuru.RestAuth.service.contract.adapter;

import me.therimuru.RestAuth.dto.requests.auth.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.responses.SignUpResult;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    /**
     *
     * @param userSignUpDTO validated DTO from controller
     * @return {@link ResponseEntity}
     * @throws UserAlreadyRegisteredException when user with provided username already registered
     */
    SignUpResult signUp(UserSignUpDTO userSignUpDTO);

    /**
     *
     * @param userSignInDTO validated DTO from controller
     * @return Refresh token as {@link String}
     * @throws UserNotFoundInDatabaseException when entry with provided username not found
     * @throws InvalidPasswordException when entry with provided username was found, but password invalid
     */
    String signIn(UserSignInDTO userSignInDTO);

    /**
     *
     * @param refreshToken refresh token
     * @return Generated access token as ${@link String}
     */
    String getAccessToken(String refreshToken);
}