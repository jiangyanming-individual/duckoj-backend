package com.jiang.duckoj.judge.codesandbox;

import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {

    /**
     * 执行代码：
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse doExecute(ExecuteCodeRequest executeCodeRequest);
}
