package me.therimuru.RestAuth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpDTO {

    @JsonProperty // cast json to DTO
    @Size(min = 2, max = 16)
    private String name;

    @JsonProperty
    @Size(min = 2, max = 32)
    private String surname;

    @JsonProperty
    @Pattern(regexp = "^[a-z]{2,16}$") // lowercase 2-16 en chars regexp
    private String login;

    @JsonProperty
    @Min(18) @Max(99)
    private Integer age;

    @Size(min = 6, max = 128)
    private String password;
}