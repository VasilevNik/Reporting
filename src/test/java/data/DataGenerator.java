package data;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;
import entities.UserInfo;

import java.util.Locale;

@UtilityClass

public class DataGenerator {

    @UtilityClass
    public static class Registration {
        public static UserInfo generateInfo(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UserInfo(faker.address().city(),
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber());
        }
    }
}
