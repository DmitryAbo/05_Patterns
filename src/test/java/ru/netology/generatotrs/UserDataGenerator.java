package ru.netology.generatotrs;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import ru.netology.users.UserInfo;

import java.util.Locale;

@UtilityClass
public class UserDataGenerator {
    @UtilityClass
    public static class Registration {
        public static UserInfo registrationInfo(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UserInfo(faker.address().cityName(),faker.name().fullName().replace("ё","e"),faker.phoneNumber().phoneNumber().replaceAll("[(,),-]", ""));
        }
    }
}
