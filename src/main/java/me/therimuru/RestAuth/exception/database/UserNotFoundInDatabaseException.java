package me.therimuru.RestAuth.exception.database;

import me.therimuru.RestAuth.dto.UserSignUpDTO;

public class UserNotFoundInDatabaseException extends RuntimeException {
    public UserNotFoundInDatabaseException(UserSignUpDTO userSignUpDTO) {
        super(userSignUpDTO.getUsername());
    }
}
