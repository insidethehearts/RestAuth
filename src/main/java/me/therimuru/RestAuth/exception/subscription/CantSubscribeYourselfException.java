package me.therimuru.RestAuth.exception.subscription;

public class CantSubscribeYourselfException extends RuntimeException {
    public CantSubscribeYourselfException() {
        super("Can't subscribe to yourself.");
    }
}
