package me.therimuru.RestAuth.service.implementation;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.mapper.UserMapper;
import me.therimuru.RestAuth.object.JwtRedisKey;
import me.therimuru.RestAuth.repository.UserRepository;
import me.therimuru.RestAuth.service.JwtService;
import me.therimuru.RestAuth.service.RedisTokenService;
import me.therimuru.RestAuth.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private JwtService jwtService;
    private RedisTokenService redisTokenService;

    private UserRepository userRepository;

    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(UserSignUpDTO userSignUpDTO) {
        final String username = userSignUpDTO.getUsername();
        if (userRepository.existsByUsername(username)) throw new UserAlreadyRegisteredException(username);

        final UserEntity user = userMapper.userSignUpDTOToUserEntity(userSignUpDTO);
        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
        userRepository.save(user);

        final String refreshToken = jwtService.generateRefreshToken(userMapper.userEntityToJwtInformationWrapper(user));
        redisTokenService.saveRefreshToken(new JwtRedisKey(user.getId(), refreshToken), jwtService.getExpirationDate(refreshToken));

        return refreshToken;
    }

    @Override
    public String regenerateRefreshToken(UserSignInDTO userSignInDTO) {
        final String username = userSignInDTO.getUsername();
        final String password = userSignInDTO.getPassword();

        final UserEntity userEntity = findByUsernameAndPassword(username, password);

        redisTokenService.invalidate(userEntity.getId());
        final String refreshToken = jwtService.generateRefreshToken(userMapper.userEntityToJwtInformationWrapper(userEntity));
        redisTokenService.saveRefreshToken(new JwtRedisKey(userEntity.getId(), refreshToken));

        return refreshToken;
    }

    public UserEntity findByUsernameAndPassword(String username, String rawPassword) throws UserNotFoundInDatabaseException, InvalidPasswordException {
        final UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundInDatabaseException(username));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new InvalidPasswordException();
        return user;
    }
}
