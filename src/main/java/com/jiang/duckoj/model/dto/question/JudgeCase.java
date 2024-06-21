package com.jiang.duckoj.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 输入输出用例对象
 */

@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
}
