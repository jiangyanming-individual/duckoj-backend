package com.jiang.duckoj.model.dto.questionsubmit;
import lombok.Data;
import java.io.Serializable;

/**
 * 题目提交请求：
 *
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 提交语言
     */
    private String submitLanguage;

    /**
     * 提交代码
     */
    private String submitCode;


    private static final long serialVersionUID = 1L;
}