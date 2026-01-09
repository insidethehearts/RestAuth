package me.therimuru.RestAuth.service.contract.adapter;

import me.therimuru.RestAuth.dto.requests.auth.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.responses.UserTokenServiceResult;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    UserTokenServiceResult signUp(UserSignUpDTO userSignUpDTO);

    UserTokenServiceResult signIn(UserSignInDTO userSignInDTO);

    String getAccessToken(String refreshToken);
}