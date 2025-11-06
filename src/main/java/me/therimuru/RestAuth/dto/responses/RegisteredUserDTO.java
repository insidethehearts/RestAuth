package me.therimuru.RestAuth.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisteredUserDTO {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private Integer age;

}