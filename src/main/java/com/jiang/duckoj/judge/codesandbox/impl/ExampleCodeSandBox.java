package com.jiang.duckoj.judge.codesandbox.impl;

import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;
import com.jiang.duckoj.model.dto.questionsubmit.JudgeInfo;
import com.jiang.duckoj.model.enums.JudgeInfoMessageEnum;
import com.jiang.duckoj.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteResponse doExecute(ExecuteRequest executeRequest) {

        log.info("执行示例代码沙箱");
        //获取请求信息：
        List<String> inputList = executeRequest.getInputList();
        String submitLanguage = executeRequest.getSubmitLanguage();
        String submitCode = executeRequest.getSubmitCode();

        //返回判题信息的结果：
        ExecuteResponse executeResponse = new ExecuteResponse();
        executeResponse.setOutputList(inputList);
        executeResponse.setMessage("判题成功");
        //提交成功：
        executeResponse.setSubmitState(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeResponse.setJudgeInfo(judgeInfo);

        return executeResponse;
    }
}
