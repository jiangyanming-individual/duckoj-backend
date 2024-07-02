package com.jiang.duckoj.judge.codesandbox.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱实现
 */
public class RemoteCodeSandBox implements CodeSandBox {
    /**
     * 发送http.post请求：
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse doExecute(ExecuteCodeRequest executeCodeRequest) {
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
        String url = "localhost:8090/executeCode";
        String response = HttpRequest.post(url).body(jsonStr).execute().body();
        return JSONUtil.toBean(response, ExecuteCodeResponse.class);
    }
}
