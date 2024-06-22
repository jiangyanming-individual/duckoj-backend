package com.jiang.duckoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.model.dto.questionsubmit.JudgeInfo;
import com.jiang.duckoj.model.entity.Question;
import com.jiang.duckoj.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
/**
 * 题目提交视图
 */
@Data
public class QuestionSubmitVO implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 提交题目信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 提交语言，枚举类
     */
    private String submitLanguage;

    /**
     * 提交代码
     */
    private String submitCode;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer submitState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户VO
     */
    private UserVO userVO;

    /**
     * 题目信息VO
     */
    private QuestionVO questionVO;


    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        //对象转json:
        if (judgeInfo !=null){
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        // json 转对象：
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(questionSubmit.getJudgeInfo(),JudgeInfo.class));
        return questionSubmitVO;
    }
}
