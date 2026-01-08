package me.therimuru.RestAuth.dto.requests.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {

    @NotNull
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]{1,15}$")
    private JsonNullable<String> name = JsonNullable.undefined();
    @NotNull
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]{1,31}$")
    private JsonNullable<String> surname = JsonNullable.undefined();
    @NotNull
    @Min(18)
    @Max(99)
    private JsonNullable<Integer> age = JsonNullable.undefined();
    @Size(max = 128)
    private JsonNullable<String> bio = JsonNullable.undefined();
    @NotNull
    @JsonProperty(value = "bio_public")
    private JsonNullable<Boolean> bioPublic = JsonNullable.undefined();

    @AssertTrue(message = "Any field must be present.")
    public boolean isAnyFieldPresent() {
        return !name.isUndefined() || !surname.isUndefined() || !age.isUndefined() || !bio.isUndefined() || !bioPublic.isUndefined();
    }

    public String getName() {
        return name.isPresent() ? name.get() : null;
    }

    public String getSurname() {
        return surname.isPresent() ? surname.get() : null;
    }

    public Integer getAge() {
        return age.isPresent() ? age.get() : null;
    }

    public String getBio() {
        return bio.isPresent() ? bio.get() : null;
    }

    public Boolean isBioPublic() {
        return bioPublic.isPresent() ? bioPublic.get() : null;
    }

    public JsonNullable<String> getNullableName() {
        return name;
    }

    public JsonNullable<String> getNullableSurname() {
        return surname;
    }

    public JsonNullable<Integer> getNullableAge() {
        return age;
    }

    public JsonNullable<String> getNullableBio() {
        return bio;
    }

    public JsonNullable<Boolean> getNullableBioPublic() {
        return bioPublic;
    }

}