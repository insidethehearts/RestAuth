package me.therimuru.RestAuth.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class UserIdAuthentication extends AbstractAuthenticationToken {

    @Getter
    private final Long id;
    private boolean authenticated = true;

    public UserIdAuthentication(Long id) {
        super(List.of());
        this.id = id;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
