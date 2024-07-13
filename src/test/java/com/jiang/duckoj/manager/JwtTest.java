package com.jiang.duckoj.manager;


import com.jiang.duckoj.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class JwtTest {



    @Test
    public void  testJwt(){


        HashMap<String, Object> map = new HashMap<>();

        map.put("id",1);
        map.put("userName","jack");
        map.put("password","123456");
        String token = JwtUtils.getToken(map);

//        String token="eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MjA2MDUyMzEsInVzZXJBY2NvdW50IjoieXVwaSJ9.St130jJGmGvYC_aY_xHPRfbLEO9cM51KYKv9ZUPCMR3SQmvasKS3HwBr7USIWmPU5he93B2FEetwdv8merUQhQ";

        boolean b = JwtUtils.verifyToken(token);

        if (!b){
            System.out.println("解析token失败");
        }
        Claims claims = JwtUtils.getClaims(token);
        System.out.println(claims.get("id"));
        System.out.println(claims.get("userAccount"));
        System.out.println(claims.get("password"));

    }
}
