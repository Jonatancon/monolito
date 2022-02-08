package com.pragma.monolito.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorsUtil {

    private static final String REGEX = "^(.+)@(.+)$";
    private static final Pattern pattern = Pattern.compile(REGEX);

    private ValidatorsUtil() {}

    public static boolean isObjectNull(Object object) {
        return object == null;
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isEmptyFile(MultipartFile file) {
        return file.isEmpty();
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
