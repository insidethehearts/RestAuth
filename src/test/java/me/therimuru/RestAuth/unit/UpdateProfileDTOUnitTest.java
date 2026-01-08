package me.therimuru.RestAuth.unit;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import me.therimuru.RestAuth.dto.requests.profile.UpdateProfileDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateProfileDTOUnitTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static Faker faker;

    @BeforeAll
    public static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        faker = new Faker();
    }

    @Test
    void validateCorrectDTO() {
        final var violations = validator.validate(randomValidDTO());
        assertThat(violations).isEmpty();
    }

    @Test()
    void validateEmptyDTO() {
        final UpdateProfileDTO emptyDTO = new UpdateProfileDTO();
        final var violations = validator.validate(emptyDTO);
        assertThat(violations).isNotEmpty();
    }

    UpdateProfileDTO randomValidDTO() {
        final Name fakerName = faker.name();

        final String name = fakerName.firstName();
        final String surname = fakerName.lastName().replaceAll("'", "");
        final int age = faker.number().numberBetween(18, 99);
        final String bio = faker.lorem().sentence(5, 3);
        final Boolean bioPublic = faker.bool().bool();

        return new UpdateProfileDTO(
                JsonNullable.of(name),
                JsonNullable.of(surname),
                JsonNullable.of(age),
                JsonNullable.of(bio),
                JsonNullable.of(bioPublic)
        );
    }
}