package com.jiang.duckoj.judge;

import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
