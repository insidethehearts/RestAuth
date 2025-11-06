package me.therimuru.RestAuth.exception.database;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String username) {
        super("User with username `%s` already registered.".formatted(username));
    }
}
