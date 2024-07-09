package com.jiang.duckoj.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 校验邮箱
 */

public class ValidEmailUtils {


    public static final String REGEX_EMAIL = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
    private static final Pattern pattern = Pattern.compile(REGEX_EMAIL);

    public static boolean validate(String email) {
        return pattern.matcher(email).matches();

    }
}
