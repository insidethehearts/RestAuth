package me.therimuru.RestAuth.exception.profile;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(Long id) {
        super("User profile with id `%d` not found.".formatted(id));
    }
}
