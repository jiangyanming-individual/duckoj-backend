package com.jiang.duckoj.model.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建题目
 *
 */
@Data
public class QuestionAddRequest implements Serializable {

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
     * 后端List 转Json数组
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例，输入输出用例（json 数组）
     */
    private JudgeCase judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;


    private static final long serialVersionUID = 1L;
}