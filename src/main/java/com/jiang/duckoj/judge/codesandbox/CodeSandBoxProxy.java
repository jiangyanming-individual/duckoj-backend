package com.jiang.duckoj.judge.codesandbox;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    /**
     * 增强代码沙箱的能力：返回ExecuteResponse
     * @param executeRequest
     * @return
     */
    @Override
    public ExecuteResponse doExecute(ExecuteRequest executeRequest) {
        log.info("代码沙箱请求内容：" + executeRequest.toString());
        ExecuteResponse executeResponse = codeSandBox.doExecute(executeRequest);
        log.info("代码沙箱响应内容：" + executeResponse);
        return executeResponse;
    }
}
