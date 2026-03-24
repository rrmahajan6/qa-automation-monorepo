package com.framework.utils;

import com.framework.models.request.PostRequest;
import com.framework.models.request.UserRequest;
import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Centralised factory for generating realistic test data using JavaFaker.
 * Ensures every test run uses fresh, unique data to prevent state conflicts.
 */
public final class TestDataFactory {

    private static final Faker FAKER = new Faker(new Locale("en"));

    private TestDataFactory() {}

    // ─── Users ────────────────────────────────────────────────────────────

    public static UserRequest randomUser() {
        return UserRequest.builder()
                .name(FAKER.name().fullName())
                .username(FAKER.name().username())
                .email(FAKER.internet().emailAddress())
                .phone(FAKER.phoneNumber().phoneNumber())
                .website(FAKER.internet().domainName())
                .address(randomAddress())
                .company(randomCompany())
                .build();
    }

    public static UserRequest minimalUser() {
        return UserRequest.builder()
                .name(FAKER.name().fullName())
                .email(FAKER.internet().emailAddress())
                .build();
    }

    public static UserRequest.Address randomAddress() {
        return UserRequest.Address.builder()
                .street(FAKER.address().streetAddress())
                .suite(FAKER.address().secondaryAddress())
                .city(FAKER.address().city())
                .zipcode(FAKER.address().zipCode())
                .geo(UserRequest.Geo.builder()
                        .lat(FAKER.address().latitude())
                        .lng(FAKER.address().longitude())
                        .build())
                .build();
    }

    public static UserRequest.Company randomCompany() {
        return UserRequest.Company.builder()
                .name(FAKER.company().name())
                .catchPhrase(FAKER.company().catchPhrase())
                .bs(FAKER.company().bs())
                .build();
    }

    // ─── Posts ────────────────────────────────────────────────────────────

    public static PostRequest randomPost(int userId) {
        return PostRequest.builder()
                .userId(userId)
                .title(FAKER.lorem().sentence(6))
                .body(FAKER.lorem().paragraph(3))
                .build();
    }

    // ─── Primitives ───────────────────────────────────────────────────────

    public static String randomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String randomName() {
        return FAKER.name().fullName();
    }

    public static int randomPositiveInt(int max) {
        return FAKER.number().numberBetween(1, max);
    }
}
