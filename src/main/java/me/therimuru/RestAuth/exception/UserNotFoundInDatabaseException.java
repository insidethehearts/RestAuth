package me.therimuru.RestAuth.exception;

import me.therimuru.RestAuth.dto.UserSignUpDTO;

public class UserNotFoundInDatabaseException extends RuntimeException {
    public UserNotFoundInDatabaseException(UserSignUpDTO userSignUpDTO) {
        super(userSignUpDTO.getLogin());
    }
}
