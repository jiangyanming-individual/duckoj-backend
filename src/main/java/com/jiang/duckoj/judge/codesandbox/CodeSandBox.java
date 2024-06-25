package com.jiang.duckoj.judge.codesandbox;

import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;

public interface CodeSandBox {

    /**
     * 执行代码：
     * @param executeRequest
     * @return
     */
    ExecuteResponse doExecute(ExecuteRequest executeRequest);
}
