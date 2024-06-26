package com.jiang.duckoj.judge;

import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.vo.QuestionSubmitVO;

/**
 * 判题服务接口
 */
public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
