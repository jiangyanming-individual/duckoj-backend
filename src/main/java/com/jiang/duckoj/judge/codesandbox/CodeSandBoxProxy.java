package com.jiang.duckoj.judge.codesandbox;

import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * 代理模式：增强类的功能：
 */
@Data
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    /**
     * 增强代码沙箱的能力：返回executeCodeResponse
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse doExecute(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求内容：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.doExecute(executeCodeRequest);
        log.info("代码沙箱响应内容：" + executeCodeResponse);
        return executeCodeResponse;
    }
}
