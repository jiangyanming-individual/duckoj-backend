package com.jiang.duckoj.judge.codesandbox.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.service.QuestionSubmitService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class testStream {


    @Resource
    QuestionSubmitService questionSubmitService;

    @Test
    void test() {
        List<Long> list = questionSubmitService.list().stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toList());
        for (Long l : list) {
            System.out.println(l);
        }

        String questionId = "1804421800622231554";
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq("questionId", questionId);
        long count = questionSubmitService.list(questionSubmitQueryWrapper).stream().count();
        System.out.println("结果：" + count);

    }

    @Test
    void test2() {
        String questionId = "1804421800622231554";
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq("questionId", questionId);
        int submitNum = (int) questionSubmitService.list(questionSubmitQueryWrapper).stream().count();
        List<QuestionSubmit> questionSubmitList = questionSubmitService.list(questionSubmitQueryWrapper).stream().collect(Collectors.toList());
        //按照状态进行分组：
        Map<Integer, List<QuestionSubmit>> submitQuestionMap = questionSubmitList.stream().collect(Collectors.groupingBy(QuestionSubmit::getSubmitState));
        int acceptedNum = submitQuestionMap.get(2).size();
        System.out.println("acceptedNum:" + acceptedNum);
    }
}
