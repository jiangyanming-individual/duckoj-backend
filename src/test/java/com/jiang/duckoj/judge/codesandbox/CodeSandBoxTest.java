package com.jiang.duckoj.judge.codesandbox;

import com.jiang.duckoj.judge.codesandbox.model.ExecuteRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteResponse;
import com.jiang.duckoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
class CodeSandBoxTest {


    //使用注入bean的方式注入类型
    @Value("${codesandbox.type}")
    private String type;

    @Test
    void testExampleCodeSandBox() {

        CodeSandBoxFactory codeSandBoxFactory = new CodeSandBoxFactory();
        CodeSandBox exampleCodeSandBox = codeSandBoxFactory.getInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(exampleCodeSandBox);
        String code = "int main()";
        String language = QuestionSubmitLanguageEnum.JAVA.getText();
        List<String> inputList = Arrays.asList("12", "34");
        ExecuteRequest executeRequest = ExecuteRequest.builder().submitCode(code).submitLanguage(language).inputList(inputList).build();
        ExecuteResponse executeResponse = codeSandBoxProxy.doExecute(executeRequest);
        Assertions.assertNull(executeResponse);
    }
}