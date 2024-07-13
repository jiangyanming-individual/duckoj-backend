package com.jiang.duckoj.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

import static com.jiang.duckoj.model.enums.RabbitMQConstant.*;

/**
 * 声明并创建交换机以及队列
 */
@Configuration
@Slf4j
public class RabbitMQConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /**
     * 创建交换机
     *
     * @return
     */

    //自动创建就是靠他
    //开启懒加载，不然会有循环依赖问题
    @Lazy
    @Resource
    RabbitAdmin rabbitAdmin;

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue createQueue() {
        //添加死信交换机和key
        HashMap<String, Object> argsMap = new HashMap<>();
        argsMap.put("x-dead-letter-exchange", DLX_DIRECT_EXCHANGE); // 设置死信交换机
        argsMap.put("x-dead-letter-routing-key", DLX_ROUTING_KEY); // 设置死信路由键

        return new Queue(DIRECT_QUEUE, true, false, false, argsMap);
    }

    // 将普通队列和Direct交换机进行绑定
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(createQueue()).to(directExchange()).with(ROUTING_KEY);
    }

    // 创建死信队列
    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_DIRECT_QUEUE);
    }

    // 创建死信交换机
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_DIRECT_EXCHANGE);
    }

    //绑定死信交换机和队列：
    @Bean
    public Binding bindingDlx() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(DLX_ROUTING_KEY);
    }

    //创建初始化RabbitAdmin对象,才会自动创建交换机和队列，不然就要用原生的方式
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    //创建交换机和对列
    @Bean
    public void createExchangeQueue() {

        //创建正常
        rabbitAdmin.declareExchange(directExchange());
        rabbitAdmin.declareQueue(createQueue());

        //创建死信
        rabbitAdmin.declareExchange(dlxExchange());
        rabbitAdmin.declareQueue(dlxQueue());
        log.info("创建队列和交互机成功！");
    }


    /**
     * 设置两个回调函数：
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    //ConfirmCallBack
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //发送的uuid
        String id = correlationData.getId();
        if (ack) {
            log.info("消息Id为：{}发送到了交换机", id);
        } else {
            log.error("消息Id为：{},没有到达交换机,失败原因：{}", id, cause);
        }
    }
    //returnCallBack
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息没有到达队列");
        log.info("交换机为：" + returnedMessage.getExchange());
        log.info("路由key:" + returnedMessage.getRoutingKey());
        log.info("消息为：" + returnedMessage.getMessage());
        log.info("ReplyText为：" + returnedMessage.getReplyText());
        log.info("ReplyCode为：" + returnedMessage.getReplyCode());
    }
}
