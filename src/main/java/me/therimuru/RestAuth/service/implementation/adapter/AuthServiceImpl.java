package me.therimuru.RestAuth.service.implementation.adapter;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.exception.jwt.JwtException;
import me.therimuru.RestAuth.object.JwtInformationWrapper;
import me.therimuru.RestAuth.object.JwtRedisKey;
import me.therimuru.RestAuth.object.TokenType;
import me.therimuru.RestAuth.service.contract.adapter.AuthService;
import me.therimuru.RestAuth.service.contract.internal.JwtService;
import me.therimuru.RestAuth.service.contract.internal.RedisTokenService;
import me.therimuru.RestAuth.service.contract.internal.UserService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private UserService userService;
    private JwtService jwtService;
    private RedisTokenService redisTokenService;

    @Override
    public String signUp(UserSignUpDTO userSignUpDTO) throws UserAlreadyRegisteredException {
        final UserEntity user = userService.register(userSignUpDTO);
        return generateAndPrepareTokenForUser(user);
    }

    @Override
    public String signIn(UserSignInDTO userSignInDTO) throws UserNotFoundInDatabaseException, InvalidPasswordException {
        final UserEntity user = userService.findBySignInDTO(userSignInDTO);
        return generateAndPrepareTokenForUser(user);
    }

    @Override
    public String getAccessToken(String refreshToken) throws JwtException {
        return null; // CREATEIT
    }

    private String generateAndPrepareTokenForUser(UserEntity user) {
        final Long userId = user.getId();
        final String username = user.getUsername();

        final String refreshToken = jwtService.generateToken(new JwtInformationWrapper(userId, username, TokenType.REFRESH));
        redisTokenService.invalidate(user.getId());
        redisTokenService.saveRefreshToken(new JwtRedisKey(userId, username));

        return refreshToken;
    }
}
