package me.therimuru.RestAuth.service.implementation;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.dto.UserSignUpDTO;
import me.therimuru.RestAuth.mapper.UserMapper;
import me.therimuru.RestAuth.repository.UserRepository;
import me.therimuru.RestAuth.service.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;

    @Override
    public boolean isRegistrable(UserSignUpDTO userSignUpDTO) {
        final String login = userSignUpDTO.getUsername();
        return !userRepository.existsByLogin(login);
    }

    @Override
    public void register(UserSignUpDTO userSignUpDTO) {
        userRepository.save(userMapper.userSignUpDTOToUserEntity(userSignUpDTO));
    }

}
