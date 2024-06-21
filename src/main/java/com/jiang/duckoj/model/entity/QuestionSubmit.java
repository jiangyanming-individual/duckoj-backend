package com.jiang.duckoj.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 提交题目表
 * @TableName question_submit
 */
@Data
public class QuestionSubmit implements Serializable {
    /**
     * id
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
    private String judgeInfo;

    /**
     * 提交语言
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
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}