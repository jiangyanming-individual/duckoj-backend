package com.jiang.duckoj.utils;


import com.alibaba.excel.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    /**
     * TOKEN的有效期1小时（S）
     */
    private static final int TOKEN_TIME_OUT = 60 * 60;

    /**
     * 加密KEY
     */
    private static final String TOKEN_SECRET = "jiangyanming";


    /**
     * 生成Token
     *
     * @param params
     * @return
     */
    public static String getToken(Map params) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                // 加密方式
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                // 过期时间戳
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))
                .addClaims(params) //payload
                .compact();
    }


    /**
     * 获取Token中的claims信息
     */
    public static Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token).getBody();
    }


    /**
     * 是否有效 true-有效，false-失效
     */
    public static boolean verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            if (claims.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}