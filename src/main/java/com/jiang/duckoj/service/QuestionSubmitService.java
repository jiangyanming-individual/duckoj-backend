package com.jiang.duckoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiang.duckoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.jiang.duckoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiang.duckoj.model.entity.User;
import com.jiang.duckoj.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiangyanming
 * @description 针对表【question_submit(提交题目表)】的数据库操作Service
 * @createDate 2024-06-21 17:43:59
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装类
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目提交封装类,进行信息脱敏
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

}
