package me.therimuru.RestAuth.utils;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import me.therimuru.RestAuth.dto.requests.auth.UserSignUpDTO;

public class UserUtils {

    private static final Faker faker;

    static {
        faker = Faker.instance();
    }

    public static UserSignUpDTO newRandomUser() {
        final Name fakerName = faker.name();

        final String name = fakerName.firstName();
        final String surname = fakerName.lastName();
        final int age = faker.number().numberBetween(18, 99);
        final String username = fakerName.username();
        final String password = faker.internet().password(12, 32, true, true, true);

        return new UserSignUpDTO(name, surname, username, age, password);
    }

}