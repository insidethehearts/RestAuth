package me.therimuru.RestAuth.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserForSubscribeNotFoundException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.exception.jwt.TokenNotFoundInRedisException;
import me.therimuru.RestAuth.object.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(exception = {UserNotFoundInDatabaseException.class, InvalidPasswordException.class, JWTVerificationException.class, TokenNotFoundInRedisException.class})
    public ResponseEntity handle401Exception(RuntimeException exception) {
        return errorResponse(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(exception = {UserAlreadyRegisteredException.class, DataIntegrityViolationException.class})
    public ResponseEntity handle409Exception(RuntimeException exception) {
        return errorResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(exception = UserForSubscribeNotFoundException.class)
    public ResponseEntity handle404Exception(RuntimeException exception) {
        return errorResponse(exception, HttpStatus.NOT_FOUND);
    }

    // HttpMediaTypeNotSupportedEx, - 400

    private ResponseEntity errorResponse(RuntimeException exception, HttpStatus httpStatus) {
        return ApiResponse
                .builder(httpStatus)
                .field("status", "error")
                .field("message", exception.getMessage())
                .build();
    }
}