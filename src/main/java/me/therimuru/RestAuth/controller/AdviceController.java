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

    @ExceptionHandler(exception = {UserNotFoundInDatabaseException.class, InvalidPasswordException.class, JwtException.class})
    public ResponseEntity handle401Exception(RuntimeException exception) {
        return errorResponse(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(exception = UserAlreadyRegisteredException.class)
    public ResponseEntity handle409Exception(RuntimeException exception) {
        return errorResponse(exception, HttpStatus.CONFLICT);
    }


    private ResponseEntity errorResponse(RuntimeException exception, HttpStatus httpStatus) {
        return ApiResponse
                .builder(httpStatus)
                .field("status", "error")
                .field("message", exception.getMessage())
                .build();
    }
}