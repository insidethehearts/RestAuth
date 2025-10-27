package me.therimuru.RestAuth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserSignUpDTO {

    @JsonProperty // cast json to DTO
    @NotNull @Size(min = 2, max = 16)
    private String name;

    @JsonProperty
    @NotNull @Size(min = 2, max = 32)
    private String surname;

    @JsonProperty
    @NotNull @Pattern(regexp = "^[a-z_]{2,16}$") // lowercase 2-16 en chars regexp
    private String username;

    @JsonProperty
    @NotNull @Min(18) @Max(99)
    private Integer age;

    @Getter @Setter
    @NotNull @Size(min = 6, max = 128)
    private String password;
}