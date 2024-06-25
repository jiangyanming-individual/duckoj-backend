package com.jiang.duckoj.judge;

import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.CodeSandBoxFactory;
import com.jiang.duckoj.judge.codesandbox.CodeSandBoxProxy;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;
import com.jiang.duckoj.model.dto.question.JudgeCase;
import com.jiang.duckoj.model.dto.question.JudgeConfig;
import com.jiang.duckoj.model.dto.questionsubmit.JudgeInfo;
import com.jiang.duckoj.model.entity.Question;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.enums.JudgeInfoMessageEnum;
import com.jiang.duckoj.model.enums.QuestionSubmitStatusEnum;
import com.jiang.duckoj.model.vo.QuestionSubmitVO;
import com.jiang.duckoj.service.QuestionService;
import com.jiang.duckoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type}")
    private String type;

    @Override
    public QuestionSubmitVO doJudge(long questionSubmitId) {
        //(1) 传入判题的id，获取到对应判题题目、判题语言、判题内容、提交信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        //参数校验：
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交题目为空");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }

        //(2) 如果题目的提交状态不为待判题，就不用重复提交判题 （只有待判题是真正需要判题的）
        if (!questionSubmit.getSubmitState().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目在判题中");
        }
        //(3) 如果判题状态不为判题中，更改判题状态为判题中。
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setSubmitState(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean save = questionSubmitService.save(questionSubmitUpdate);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目提交状态失败");
        }
        //(4) 调用代码沙箱，得到判题结果。
        //工厂+ 代理模式：
        CodeSandBoxFactory codeSandBoxFactory = new CodeSandBoxFactory();
        CodeSandBox exampleCodeSandBox = codeSandBoxFactory.getInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(exampleCodeSandBox);
        String submitLanguage = questionSubmit.getSubmitLanguage();
        String submitCode = questionSubmit.getSubmitCode();
        //题目判题用例：
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        //判题输入用例：
        List<String> judgeCaseInput = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        //判题输出用例：
        List<String> judgeCaseOutput = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        ExecuteRequest executeRequest = ExecuteRequest.builder()
                .submitCode(submitCode)
                .submitLanguage(submitLanguage)
                .inputList(judgeCaseInput).build();
        ExecuteResponse executeResponse = codeSandBoxProxy.doExecute(executeRequest);

        //(5) 判断条件：
        // 1. 判断输入用例和代码沙箱的输出个数是否相等。
        //设置判题的状态：
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
        List<String> outputList = executeResponse.getOutputList();
        if (judgeCaseInput.size() != outputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            return null;
        }
        // 2. 判断每个输出用例和代码沙箱的输出是否相等，如果不相等直接返回。
        for (int i = 0; i < judgeCaseOutput.size(); i++) {
            if (!judgeCaseOutput.get(i).equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                return null;
            }
        }
        // 3. 判断题目的限制是否符合要求。
        JudgeInfo judgeInfo = executeResponse.getJudgeInfo();
        Long runTime = judgeInfo.getTime();
        Long runMemory = judgeInfo.getMemory();
        //题目设置的判题限制：
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig expectJudgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needTimeLimit = expectJudgeConfig.getTimeLimit();
        Long needeMoryLimit = expectJudgeConfig.getMemoryLimit();
        if (runTime > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            return null;
        }
        if (runMemory > needeMoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            return null;
        }
        //6 更新提交题目状态以及判题的状态
        judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        questionSubmitUpdate.setSubmitState(QuestionSubmitStatusEnum.SUCCEED.getValue());
        boolean update = questionSubmitService.save(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新提交题目状态失败");
        }
        return null;
    }
}
