package com.jiang.duckoj.judge.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.judge.strategy.JudgeContext;
import com.jiang.duckoj.judge.strategy.JudgeStrategy;
import com.jiang.duckoj.model.dto.question.JudgeCase;
import com.jiang.duckoj.model.dto.question.JudgeConfig;
import com.jiang.duckoj.model.dto.questionsubmit.JudgeInfo;
import com.jiang.duckoj.model.entity.Question;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * java的判题策略
 */
public class JavaJudgeStrategy implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //(5) 判断条件：
        // 1. 判断输入用例和代码沙箱的输出个数是否相等。
        //设置判题的状态：
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        List<String> judgeCaseInput = judgeContext.getJudgeCaseInput();
        List<String> judgeCaseOutput = judgeContext.getJudgeCaseOutput();
        List<String> outputList = judgeContext.getOutputList();
        //判题输出信息：
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long runTime = judgeInfo.getTime();
        Long runMemory = judgeInfo.getMemory();

        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        Question question = judgeContext.getQuestion();
        //设置返回对象：
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(runTime);
        judgeInfoResponse.setMemory(runMemory);

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        if (judgeCaseInput.size() != outputList.size()) {
            //设置判题结果信息
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 2. 判断每个输出用例和代码沙箱的输出是否相等，如果不相等直接返回。
        for (int i = 0; i < judgeCaseOutput.size(); i++) {
            if (!judgeCaseOutput.get(i).equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 3. 判断题目的限制是否符合要求。
        //题目设置的判题限制：
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig expectJudgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needTimeLimit = expectJudgeConfig.getTimeLimit();
        Long needeMoryLimit = expectJudgeConfig.getMemoryLimit();

        //java 策略额外需要花费10s:
        final long JAVA_COST_TIME = 10000L;
        if ((runTime - JAVA_COST_TIME) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (runMemory > needeMoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //成功后返回判题成功的信息：
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
