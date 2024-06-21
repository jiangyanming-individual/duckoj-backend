package com.jiang.duckoj.controller;

import com.jiang.duckoj.common.BaseResponse;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.common.ResultUtils;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.jiang.duckoj.model.entity.User;
import com.jiang.duckoj.service.QuestionSubmitService;
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
@RequestMapping("/questionSubmit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;


    /**
     * 提交题目
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum
     */
    @PostMapping("/submit")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数不存在");
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        if (loginUser.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户id不合法");
        }
        //返回提交题目后插入数据中的id
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

}
