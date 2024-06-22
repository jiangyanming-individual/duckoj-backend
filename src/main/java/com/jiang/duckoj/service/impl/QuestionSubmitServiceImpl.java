package com.jiang.duckoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.constant.CommonConstant;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.mapper.QuestionMapper;
import com.jiang.duckoj.model.dto.questionsubmit.JudgeInfo;
import com.jiang.duckoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.jiang.duckoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.jiang.duckoj.model.entity.Question;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import com.jiang.duckoj.model.entity.User;
import com.jiang.duckoj.model.enums.QuestionSubmitLanguageEnum;
import com.jiang.duckoj.model.enums.QuestionSubmitStatusEnum;
import com.jiang.duckoj.model.vo.QuestionSubmitVO;
import com.jiang.duckoj.model.vo.UserVO;
import com.jiang.duckoj.service.QuestionService;
import com.jiang.duckoj.service.QuestionSubmitService;
import com.jiang.duckoj.mapper.QuestionSubmitMapper;
import com.jiang.duckoj.service.UserService;
import com.jiang.duckoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jiangyanming
 * @description 针对表【question_submit(提交题目表)】的数据库操作Service实现
 * @createDate 2024-06-21 17:43:59
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    /***
     * 提交题目
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        long userId = loginUser.getId();
        //数据库中查询user
        User oldUser = userService.getById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户不存在");
        }
        //参数校验：
        QuestionSubmit questionSubmit = new QuestionSubmit();
        long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        String submitLanguage = questionSubmitAddRequest.getSubmitLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(submitLanguage);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        String submitCode = questionSubmitAddRequest.getSubmitCode();
        //保存到对象中
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setSubmitCode(submitCode);
        questionSubmit.setSubmitLanguage(submitLanguage);
        questionSubmit.setUserId(userId);
        questionSubmit.setSubmitState(QuestionSubmitStatusEnum.WATING.getValue());//设置提交状态
        questionSubmit.setJudgeInfo("{}");
        //插入数据：
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交题目失败");
        }
        //返回提交题目后的id：
        return questionSubmit.getId();
    }

    /**
     * 拼接查询参数
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        //为空返回空的查询
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String submitLanguage = questionSubmitQueryRequest.getSubmitLanguage();
        Integer submitState = questionSubmitQueryRequest.getSubmitState();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        //拼接查询字段：
        queryWrapper.eq(StringUtils.isNotBlank(submitLanguage), "submitLanguage", submitLanguage) ;
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(submitState) != null, "submitState", submitState);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId",userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId),"questionId",questionId);
        //排序字段：
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        return queryWrapper;
    }

    /**
     * 获取单个题目提交信息：
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        if (questionSubmit  == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目提交参数为空");
        }
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long userId = loginUser.getId();
        //不是管理员而且不是提交者，不能查看代码：
        if (userId!=questionSubmitVO.getUserId() && !userService.isAdmin(loginUser)){
            //代码进行脱敏
            questionSubmitVO.setSubmitCode(null);
        }
        return questionSubmitVO;

    }

    /**
     * 分页脱敏信息：
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        //如果为空返回空的分页数据：
        if (CollUtil.isEmpty(questionSubmitList)){
            return questionSubmitVOPage;
        }
        //关联查询数据；
//        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
//        List<User> userList = userService.listByIds(userIdSet);
//        Map<Long, List<User>> userIdUserListMap = userList.stream().collect(Collectors.groupingBy(User::getId));
//
//        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
//            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
//            long userId = questionSubmit.getUserId();
//            User user = null;
//            //查询hashmap:
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//            UserVO userVO = userService.getUserVO(user);
//            questionSubmitVO.setUserVO(userVO);
//            return questionSubmitVO;
//        }).collect(Collectors.toList());


        //调用上面的单个提交信息脱敏的api进行脱敏：
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            return getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());
        //设置分页的数据：
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




