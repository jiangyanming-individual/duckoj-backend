package com.jiang.duckoj.judge.codesandbox.impl;

import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;

public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteResponse doExecute(ExecuteRequest executeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
