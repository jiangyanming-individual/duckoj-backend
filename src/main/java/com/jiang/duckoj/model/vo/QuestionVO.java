package com.jiang.duckoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.model.dto.question.JudgeCase;
import com.jiang.duckoj.model.dto.question.JudgeConfig;
import com.jiang.duckoj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 */
@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 创建题目用户 id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;


    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();

        BeanUtils.copyProperties(questionVO, question);
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        List<String> tagList = questionVO.getTags();
        //List 转Json 字符串：
        if (tagList !=null){
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        //对象转 json
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        String judgeConfig= question.getJudgeConfig();
        // json 转对象：
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        //json 转List
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        return questionVO;
    }
}
