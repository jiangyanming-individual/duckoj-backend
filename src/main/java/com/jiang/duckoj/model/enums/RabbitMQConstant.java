package com.jiang.duckoj.model.enums;

/**
 * rabbitmq 常量
 */
public interface RabbitMQConstant {

    String DIRECT_EXCHANGE = "direct_questionSubmit_exchange";
    String DIRECT_QUEUE = "direct_questionSubmit_queue";
    String ROUTING_KEY = "question_submit_routingKey";


    String DLX_DIRECT_EXCHANGE = "dlx_questionSubmit_exchange";
    String DLX_DIRECT_QUEUE = "dlx_questionSubmit_queue";
    String DLX_ROUTING_KEY = "dlx_questionSubmit_routingKey";
}
