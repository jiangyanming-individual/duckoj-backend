package com.jiang.duckoj.judge;

import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.CodeSandBoxFactory;
import com.jiang.duckoj.judge.codesandbox.CodeSandBoxProxy;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;
import com.jiang.duckoj.judge.strategy.JudgeContext;
import com.jiang.duckoj.judge.strategy.JudgeStrategy;
import com.jiang.duckoj.judge.strategy.impl.DefaultJudgeStrategy;
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
    public QuestionSubmit doJudge(long questionSubmitId) {
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

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setJudgeCaseInput(judgeCaseInput);
        judgeContext.setJudgeCaseOutput(judgeCaseOutput);
        //获取代码沙箱输出结果
        judgeContext.setOutputList(executeResponse.getOutputList());
        //获得代码沙箱输出的判题的信息：
        judgeContext.setJudgeInfo(executeResponse.getJudgeInfo());
        judgeContext.setQuestionSubmit(questionSubmit);
        judgeContext.setQuestion(question);
        //使用默认的判题策略
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        //返回判题的信息：执行判题：
        JudgeInfo judgeInfo = judgeStrategy.doJudge(judgeContext);

        //6 更新提交题目状态以及判题的状态
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setSubmitState(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean update = questionSubmitService.save(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目提交状态失败");
        }
        //返回提交题目信息
        return questionSubmitService.getById(questionSubmitId);
    }
}
