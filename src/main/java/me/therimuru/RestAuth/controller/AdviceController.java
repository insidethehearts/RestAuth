package me.therimuru.RestAuth.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import me.therimuru.RestAuth.exception.database.InvalidPasswordException;
import me.therimuru.RestAuth.exception.database.UserAlreadyRegisteredException;
import me.therimuru.RestAuth.exception.database.UserForSubscribeNotFoundException;
import me.therimuru.RestAuth.exception.database.UserNotFoundInDatabaseException;
import me.therimuru.RestAuth.exception.jwt.TokenNotFoundInRedisException;
import me.therimuru.RestAuth.exception.profile.UserProfileNotFoundException;
import me.therimuru.RestAuth.object.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(exception = {UserNotFoundInDatabaseException.class, InvalidPasswordException.class, JWTVerificationException.class, TokenNotFoundInRedisException.class})
    public ResponseEntity handle401Exception(RuntimeException exception) {
        return errorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(exception = {UserAlreadyRegisteredException.class, DataIntegrityViolationException.class})
    public ResponseEntity handle409Exception(RuntimeException exception) {
        return errorResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(exception = UserForSubscribeNotFoundException.class)
    public ResponseEntity handle404Exception(RuntimeException exception) {
        return errorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity handleInvalidBodyException(MethodArgumentNotValidException exception) {
        final List<String> badFields = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            badFields.add(error.getField());
        }
        return errorResponse("Bad fields. %s".formatted(badFields), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = UserProfileNotFoundException.class)
    public ResponseEntity handleUserProfileNotFoundException(UserProfileNotFoundException exception) {
        return errorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    // HttpMediaTypeNotSupportedEx, - 400

    private ResponseEntity errorResponse(String message, HttpStatus httpStatus) {
        return ApiResponse
                .builder(httpStatus)
                .field("status", "error")
                .field("message", message)
                .build();
    }
}