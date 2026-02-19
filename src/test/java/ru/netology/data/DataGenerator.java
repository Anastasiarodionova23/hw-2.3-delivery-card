package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private DataGenerator() {}

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        String[] cities = {"Москва", "Санкт-Петербург", "Казань", "Новосибирск", "Екатеринбург"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.phoneNumber().phoneNumber();
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

    public static UserInfo generateUser() {
        return new UserInfo(generateCity(), generateName(), generatePhone());
    }
}