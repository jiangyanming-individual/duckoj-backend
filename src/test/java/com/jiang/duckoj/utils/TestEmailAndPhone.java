package com.jiang.duckoj.utils;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TestEmailAndPhone {

    @Resource
    private ValidEmailUtils validEmailUtils;

    @Resource
    private ValidPhoneUtils validPhoneUtils;

    @Test
    public void test() {

        String email = "12345678";
        boolean validate = validEmailUtils.validate(email);
        System.out.println(validate);
    }


    @Test
    public void testPhone() {

        String phone = "12345678458454545";
        boolean validate = validPhoneUtils.validate(phone);
        System.out.println(validate);
    }
}
