package me.therimuru.RestAuth.controller;

import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.exception.jwt.JwtException;
import me.therimuru.RestAuth.object.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(exception = {UserNotFoundInDatabaseException.class, InvalidPasswordException.class, UserAlreadyRegisteredException.class, JwtException.class})
    public ResponseEntity handleException(RuntimeException exception) {
        return ApiResponse
                .builder(HttpStatus.BAD_REQUEST)
                .field("status", "Error")
                .field("message", exception.getMessage())
                .build();
    }

}