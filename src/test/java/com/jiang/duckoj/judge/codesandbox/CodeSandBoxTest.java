package com.jiang.duckoj.judge.codesandbox;

import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.jiang.duckoj.judge.codesandbox.model.ExecuteCodeResponse;
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
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().submitCode(code).submitLanguage(language).inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.doExecute(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }


    @Test
    void testRemoteCodeSandBox() {
        CodeSandBoxFactory codeSandBoxFactory = new CodeSandBoxFactory();
        CodeSandBox remoteCodeSandBox = codeSandBoxFactory.getInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(remoteCodeSandBox);
        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果：\" + (a + b));\n" +
                "    }\n" +
                "}\n";
        String language = QuestionSubmitLanguageEnum.JAVA.getText();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest
                .builder()
                .submitCode(code)
                .submitLanguage(language)
                .inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.doExecute(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}