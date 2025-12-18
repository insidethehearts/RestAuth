package me.therimuru.RestAuth.dto.requests;

import jakarta.validation.constraints.*;
import lombok.*;
import me.therimuru.RestAuth.dto.DataTransferObject;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserSignUpDTO implements DataTransferObject {

    @NotNull
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]{1,15}$")
    private String name;

    @NotNull
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]{1,31}$")
    private String surname;

    @NotNull
    @Pattern(regexp = "^[a-z0-9_.]{2,16}$")
    private String username;

    @NotNull
    @Min(18)
    @Max(99)
    private Integer age;

    @NotNull
    @Size(min = 6, max = 128)
    private String password;

    @Override
    public String debugString() {
        return "%s:%s:%s:%d".formatted(name, surname, username, age);
    }

}