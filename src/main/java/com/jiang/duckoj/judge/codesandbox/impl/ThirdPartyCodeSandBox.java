package com.jiang.duckoj.judge.codesandbox.impl;

import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;

/**
 * 第三方代码沙箱
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteResponse doExecute(ExecuteRequest executeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
