package com.jiang.duckoj.manager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 限流测试
 */
@SpringBootTest
public class DoRateLimit {

    /**
     * @author Lenovo
     * @date 2024/4/21
     * @time 9:55
     * @project springboot-init
     **/
        @Resource
        private RedissonRateLimiterManager redissonLimitRateManager;

        @Test
        public void doRateLimitTets() throws InterruptedException {
            String userId="1";

            for (int i = 0; i <2 ; i++) {
                redissonLimitRateManager.doLimitRate(userId);
                System.out.println("成功");
            }
            Thread.sleep(1000);
            for (int i = 0;i< 5; i++){
                redissonLimitRateManager.doLimitRate(userId);
                System.out.println("成功");
            }
        }

}
