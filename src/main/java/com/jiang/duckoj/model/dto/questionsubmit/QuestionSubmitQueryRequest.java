package com.jiang.duckoj.model.dto.questionsubmit;

import com.jiang.duckoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 提交语言，枚举类
     */
    private String submitLanguage;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer submitState;


    private static final long serialVersionUID = 1L;
}