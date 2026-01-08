package me.therimuru.RestAuth.service.contract.internal;

import me.therimuru.RestAuth.dto.requests.auth.UserSignInDTO;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;
import me.therimuru.RestAuth.dto.requests.profile.UpdateProfileDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;

import java.util.Optional;

public interface UserService {

    UserEntity register(UserSignUpDTO userSignUpDTO) throws UserAlreadyRegisteredException;

    UserEntity findByUsernameAndPassword(String username, String rawPassword) throws UserNotFoundInDatabaseException, InvalidPasswordException;

    UserEntity findBySignInDTO(UserSignInDTO signInDTO) throws UserNotFoundInDatabaseException, InvalidPasswordException;

    Optional<UserEntity> findByIdSafe(Long id) throws UserNotFoundInDatabaseException;

    boolean existsById(Long id);

    void edit(Long id, UpdateProfileDTO updateProfileDto);
}