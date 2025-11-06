package me.therimuru.RestAuth.exception.database;

public class UserNotFoundInDatabaseException extends RuntimeException {
    public UserNotFoundInDatabaseException(String username) {
        super("User with username `%s` not found.".formatted(username));
    }
}
