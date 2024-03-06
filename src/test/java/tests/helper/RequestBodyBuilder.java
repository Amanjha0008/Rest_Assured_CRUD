package tests.helper;

import com.github.javafaker.Faker;
import java.util.Random;

// Here I am defining all the dynamic variables that is changing dynamically and i am using it in my test file " TestMain.java"
public class RequestBodyBuilder {
    public static String createUserRequestBody(String name, String email, String gender, String status) {
        return "{"
                + "\"name\": \"" + name + "\","
                + "\"email\": \"" + email + "\","
                + "\"gender\": \"" + gender + "\","
                + "\"status\": \"" + status + "\""
                + "}";
    }
    public static String generateRandomName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    public static String generateRandomEmail() {
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }
    public enum Gender {
        MALE,
        FEMALE;

        public static Gender getRandomGender() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    public enum Status {
        ACTIVE,
        INACTIVE;

        public static Status getRandomStatus() {
            Status[] statuses = Status.values();
            Random random = new Random();
            return statuses[random.nextInt(statuses.length)];
        }
    }
    public static String updateUserRequestBody(String name, String email, String gender, String status) {
        return "{"
                + "\"name\": \"" + name + "\","
                + "\"email\": \"" + email + "\","
                + "\"gender\": \"" + gender+ "\","
                + "\"status\": \"" + status + "\""
                + "}";
    }
    public static String updateUserPartialData(String name, String email, String status) {
        return "{"
                + "\"name\": \"" + name + "\","
                + "\"email\": \"" + email + "\","
                + "\"status\": \"" + status + "\""

                + "}";

    }

}
