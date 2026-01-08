package me.therimuru.RestAuth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccessTokenResponseDTO {

    @JsonProperty(value = "access_token")
    private String accessToken;

}