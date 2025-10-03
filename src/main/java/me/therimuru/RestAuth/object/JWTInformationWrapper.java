package me.therimuru.RestAuth.object;

public record JWTInformationWrapper(
   Long id,
   String login
) {
    @Override
    public String toString() {
        return "JWTInformationWrapper{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}