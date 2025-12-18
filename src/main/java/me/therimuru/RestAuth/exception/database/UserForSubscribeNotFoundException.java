package me.therimuru.RestAuth.exception.database;

public class UserForSubscribeNotFoundException extends RuntimeException {
    public UserForSubscribeNotFoundException(Long id) {
        super("User with id `%d` not found for subscribe.".formatted(id));
    }
}
