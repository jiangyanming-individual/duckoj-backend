package com.jiang.duckoj.judge.codesandbox.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jiang.duckoj.common.ErrorCode;
import com.jiang.duckoj.exception.BusinessException;
import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

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
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRETKEY = "jiangyanming";

    @Override
    public ExecuteCodeResponse doExecute(ExecuteCodeRequest executeCodeRequest) {
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
        String url = "localhost:8090/executeCode";
        //加密密钥，然后放入请求头中
        String Encode = DigestUtil.md5Hex(AUTH_REQUEST_SECRETKEY);
        String response = HttpRequest.post(url).header(AUTH_REQUEST_HEADER, Encode).body(jsonStr).execute().body();
        if (StringUtils.isBlank(response)) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR, "代码沙箱请求错误");
        }
        return JSONUtil.toBean(response, ExecuteCodeResponse.class);
    }
}
