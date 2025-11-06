package me.therimuru.RestAuth.exception.database;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Password invalid.");
    }
}
