package me.therimuru.RestAuth.exception.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import me.therimuru.RestAuth.object.TokenType;

public class BadTokenTypeException extends JWTVerificationException {
    public BadTokenTypeException(TokenType tokenType) {
        super("Bad token type. Expected %s.".formatted(tokenType));
    }
}
