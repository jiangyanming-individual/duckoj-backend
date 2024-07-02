package com.jiang.duckoj.judge.strategy;

import com.jiang.duckoj.model.dto.question.JudgeCase;
import com.jiang.duckoj.judge.codesandbox.model.JudgeInfo;
import com.jiang.duckoj.model.entity.Question;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 判题上下文，类似于DTO
 */

@Data
public class JudgeContext {

    private List<JudgeCase> judgeCaseList;

    private List<String> judgeCaseInput;

    private List<String> judgeCaseOutput;

    private List<String> outputList;

    private JudgeInfo judgeInfo;

    private QuestionSubmit questionSubmit;

    private Question question;
}
