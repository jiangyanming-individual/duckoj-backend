package com.jiang.duckoj.service;

import com.jiang.duckoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiang.duckoj.model.entity.User;

/**
* @author jiangyanming
* @description 针对表【question_submit(提交题目表)】的数据库操作Service
* @createDate 2024-06-21 17:43:59
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交题目
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

}
