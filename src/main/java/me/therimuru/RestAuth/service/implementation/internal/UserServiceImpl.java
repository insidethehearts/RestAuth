package me.therimuru.RestAuth.service.implementation.internal;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.requests.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.mapper.UserMapper;
import me.therimuru.RestAuth.repository.UserRepository;
import me.therimuru.RestAuth.service.contract.internal.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity register(UserSignUpDTO userSignUpDTO) throws UserAlreadyRegisteredException {
        final String username = userSignUpDTO.getUsername();
        if (userRepository.existsByUsername(username)) throw new UserAlreadyRegisteredException(username);

        final UserEntity user = userMapper.userSignUpDTOToUserEntity(userSignUpDTO);
        user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserEntity findByUsernameAndPassword(String username, String rawPassword) throws UserNotFoundInDatabaseException, InvalidPasswordException {
        final UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundInDatabaseException(username));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new InvalidPasswordException();
        return user;
    }

    @Override
    public UserEntity findBySignInDTO(UserSignInDTO signInDTO) throws UserNotFoundInDatabaseException, InvalidPasswordException {
        return findByUsernameAndPassword(signInDTO.getUsername(), signInDTO.getPassword());
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
