package com.jiang.duckoj.mq;

import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.judge.JudgeService;
import com.jiang.duckoj.judge.codesandbox.model.JudgeInfo;
import com.jiang.duckoj.model.entity.Question;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.enums.JudgeInfoMessageEnum;
import com.jiang.duckoj.service.QuestionService;
import com.jiang.duckoj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import javax.annotation.Resource;
import java.io.IOException;

import static com.jiang.duckoj.model.enums.RabbitMQConstant.DIRECT_QUEUE;

/**
 * 消费者执行判题消费
 */

@Component
@Slf4j
public class MyConsumer {


    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    //@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag 开启手动确认后，需要根据消息头确认
    @RabbitListener(queues = DIRECT_QUEUE, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("消费者收到发送发送来的消息");
        if (StringUtils.isBlank(message)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
        }
        long questionSubmitId = Long.parseLong(message);
        try {
            //判题：
            judgeService.doJudge(questionSubmitId);
            //更新题目通过的状态；
            QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
            String judgeInfo = questionSubmit.getJudgeInfo();
            long questionId = questionSubmit.getQuestionId();
            JudgeInfo judgeInfoBean = JSONUtil.toBean(judgeInfo, JudgeInfo.class);
            //如果判题成功；通过数加1
            if (judgeInfoBean.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
                Question question = questionService.getById(questionId);
                int acceptedNum = question.getAcceptedNum();
                Question updateQuestion = new Question();
                //更新数据库
                synchronized (question.getAcceptedNum()) {
                    acceptedNum += acceptedNum;
                    updateQuestion.setId(questionId);
                    updateQuestion.setAcceptedNum(acceptedNum);
                    boolean b = questionService.updateById(updateQuestion);
                    if (!b) {
                        throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目通过数失败");
                    }
                }
            }
            //确认收到消息
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.info("消费者消费消息失败，进入死信队列");
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
