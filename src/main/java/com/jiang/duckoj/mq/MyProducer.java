package com.jiang.duckoj.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

/**
 * 生产者
 */
@Component
@Slf4j
public class MyProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产者生产消息
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        log.info("生产者发送了一条消息");
        String mesId = UUID.randomUUID().toString(); //用于消息回调
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData(mesId));
    }
}
