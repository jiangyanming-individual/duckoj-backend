package com.jiang.duckoj.judge;

import com.jiang.duckoj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmitVO doJudge(long questionSubmitId);
}
