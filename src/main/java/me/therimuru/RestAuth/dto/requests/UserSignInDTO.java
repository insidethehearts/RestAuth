package me.therimuru.RestAuth.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.therimuru.RestAuth.dto.DataTransferObject;

@Getter
@AllArgsConstructor
public class UserSignInDTO implements DataTransferObject {

    @NotNull
    @Pattern(regexp = "^[a-z0-9_.]{2,16}$")
    private String username;

    @NotNull
    @Size(min = 6, max = 128)
    private String password;

    @Override
    public String debugString() {
        return username;
    }
}