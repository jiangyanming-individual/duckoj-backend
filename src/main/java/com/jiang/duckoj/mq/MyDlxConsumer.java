package com.jiang.duckoj.mq;


import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.judge.JudgeService;
import com.jiang.duckoj.judge.codesandbox.model.JudgeInfo;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.enums.JudgeInfoMessageEnum;
import com.jiang.duckoj.model.enums.QuestionSubmitStatusEnum;
import com.jiang.duckoj.service.QuestionService;
import com.jiang.duckoj.service.QuestionSubmitService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.jiang.duckoj.model.enums.RabbitMQConstant.DLX_DIRECT_QUEUE;

/**
 * 死信队列处理：
 */
@Component
@Slf4j
public class MyDlxConsumer {


    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @RabbitListener(queues = DLX_DIRECT_QUEUE, ackMode = "MANUAL")
    public void dlxReceiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
        }
        long questionSubmitId = Long.parseLong(message);
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交题目不存在");
        }
        //将判题机状态设置为判题失败，题目判题结果设置为失败；
        String judgeInfo = questionSubmit.getJudgeInfo();
        JudgeInfo judgeInfoBean = JSONUtil.toBean(judgeInfo, JudgeInfo.class);
        judgeInfoBean.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoBean));
        questionSubmit.setSubmitState(QuestionSubmitStatusEnum.FAILED.getValue());
        boolean b = questionSubmitService.updateById(questionSubmit);
        if (!b) {
            log.info("死信队列处理消息失败！题目Id{}", questionSubmitId);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新数据库失败");
        }
        //默认死信队列是确认收到消息
        channel.basicAck(deliveryTag, false);
    }
}
