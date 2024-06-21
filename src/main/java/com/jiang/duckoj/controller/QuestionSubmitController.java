package com.jiang.duckoj.controller;

import com.jiang.duckoj.common.BaseResponse;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.common.ResultUtils;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.model.dto.questionSubmitthumb.QuestionSubmitThumbAddRequest;
import com.jiang.duckoj.model.entity.User;
import com.jiang.duckoj.service.QuestionSubmitThumbService;
import com.jiang.duckoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 */
@RestController
@RequestMapping("/questionSubmit_thumb")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitThumbService questionSubmitThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionSubmitThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionSubmitThumbAddRequest questionSubmitThumbAddRequest,
            HttpServletRequest request) {
        if (questionSubmitThumbAddRequest == null || questionSubmitThumbAddRequest.getQuestionSubmitId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitThumbAddRequest.getQuestionSubmitId();
        int result = questionSubmitThumbService.doQuestionSubmitThumb(questionSubmitId, loginUser);
        return ResultUtils.success(result);
    }

}
