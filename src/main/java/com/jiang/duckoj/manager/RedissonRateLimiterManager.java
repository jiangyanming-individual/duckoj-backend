package com.jiang.duckoj.manager;


import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.config.RedissonConfig;
import com.jiang.duckoj.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedissonRateLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    public void doLimitRate(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        //每秒限制两次操作：
        //参数1：限流的类型， 参数2 限流的次数， 参数3：限流的时间间隔，参数4：限流的时间单位；
        //设置每秒两次：
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        //尝试获取权限：
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交过于频繁，请稍后重试");
        }
    }
}


