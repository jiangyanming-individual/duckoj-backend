package com.jiang.duckoj.utils;


import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestEncode {


    @Test
    void testEncode(){
        final String testStr="jiangyanming";
        String md5Hex1 = DigestUtil.md5Hex(testStr);
        System.out.println(md5Hex1);
    }
}
