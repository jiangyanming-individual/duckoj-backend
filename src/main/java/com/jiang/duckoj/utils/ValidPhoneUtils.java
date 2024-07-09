package com.jiang.duckoj.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 校验手机号
 */

public class ValidPhoneUtils {

    private static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    public static boolean validate(String phone) {
        return pattern.matcher(phone).matches();

    }
}
