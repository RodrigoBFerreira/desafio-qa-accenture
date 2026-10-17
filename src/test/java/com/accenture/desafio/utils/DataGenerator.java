package com.accenture.desafio.utils;

import net.datafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Geração de massa de dados aleatória.
 */
public final class DataGenerator {

    public static final Faker FAKER = new Faker(Locale.US);
    private static final Random RANDOM = new Random();

    private DataGenerator() {
    }

    public static String username() {
        return "qa_" + FAKER.name().firstName().toLowerCase() + "_" + System.currentTimeMillis();
    }

    /** Senha que atende à política do demoqa: maiúscula, minúscula, número, especial e 8+ caracteres. */
    public static String strongPassword() {
        return "Qa@" + FAKER.number().digits(4) + FAKER.letterify("??????").toLowerCase() + "Z!";
    }

    public static String firstName() {
        return FAKER.name().firstName().replaceAll("[^A-Za-z]", "");
    }

    public static String lastName() {
        return FAKER.name().lastName().replaceAll("[^A-Za-z]", "");
    }

    public static String uniqueEmail() {
        return "qa." + System.nanoTime() + "@teste.com";
    }

    public static String mobile() {
        return FAKER.number().digits(10);
    }

    public static int age() {
        return FAKER.number().numberBetween(18, 70);
    }

    public static int salary() {
        return FAKER.number().numberBetween(1000, 20000);
    }

    public static String department() {
        return pick(List.of("Insurance", "Compliance", "Legal", "Engineering", "Finance", "Marketing"));
    }

    public static String address() {
        return FAKER.address().streetAddress() + ", " + FAKER.address().city();
    }

    public static <T> T pick(List<T> options) {
        return options.get(RANDOM.nextInt(options.size()));
    }

    public static int between(int minInclusive, int maxInclusive) {
        return minInclusive + RANDOM.nextInt(maxInclusive - minInclusive + 1);
    }
}
