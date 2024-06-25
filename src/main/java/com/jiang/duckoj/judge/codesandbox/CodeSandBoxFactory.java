package com.jiang.duckoj.judge.codesandbox;

import com.jiang.duckoj.judge.codesandbox.CodeSandBox;
import com.jiang.duckoj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.jiang.duckoj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.jiang.duckoj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 静态工厂模式
 */
public class CodeSandBoxFactory {

    public static CodeSandBox getInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
