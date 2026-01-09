package me.therimuru.RestAuth.service.implementation.adapter;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.requests.auth.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.responses.UserTokenServiceResult;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.exception.jwt.TokenNotFoundInRedisException;
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
    public UserTokenServiceResult signUp(UserSignUpDTO userSignUpDTO) throws UserAlreadyRegisteredException {
        final UserEntity user = userService.register(userSignUpDTO);
        final String token = generateAndPrepareTokenForUser(user);
        return new UserTokenServiceResult(user, token);
    }

    @Override
    public UserTokenServiceResult signIn(UserSignInDTO userSignInDTO) throws UserNotFoundInDatabaseException, InvalidPasswordException {
        final UserEntity user = userService.findBySignInDTO(userSignInDTO);
        final String token = generateAndPrepareTokenForUser(user);
        return new UserTokenServiceResult(user, token);
    }

    @Override
    public String getAccessToken(String refreshToken) {
        final JwtInformationWrapper refreshTokenInfo = jwtService.decodeToken(refreshToken, TokenType.REFRESH);
        if (!redisTokenService.getRefreshToken(refreshTokenInfo.id()).token().equals(refreshToken)) {
            throw new TokenNotFoundInRedisException();
        }
        final String accessToken = jwtService.generateToken(new JwtInformationWrapper(refreshTokenInfo.id(), refreshTokenInfo.username(), TokenType.ACCESS));
        return accessToken;
    }

    private String generateAndPrepareTokenForUser(UserEntity user) {
        final Long userId = user.getId();
        final String username = user.getUsername();

        final String refreshToken = jwtService.generateToken(new JwtInformationWrapper(userId, username, TokenType.REFRESH));
        redisTokenService.invalidate(user.getId());
        redisTokenService.saveRefreshToken(new JwtRedisKey(userId, refreshToken));

        return refreshToken;
    }
}
