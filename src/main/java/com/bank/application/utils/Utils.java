package com.bank.application.utils;

import jakarta.validation.ValidationException;

import java.util.Random;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Random RANDOM = new Random();

    public static void validateEmail(String email) {
        if (EMAIL.matcher(email).matches()) {
            throw new ValidationException("Email is not valid");
        }
    }

    public static String generateAccountNumber() {
        return generateRandomNumber(16);
    }

    private static String generateRandomNumber(int len) {
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int digit = RANDOM.nextInt(10);
            number.append(digit);
        }

        return number.toString();
    }
}