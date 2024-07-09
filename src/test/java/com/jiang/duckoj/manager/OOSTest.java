package com.jiang.duckoj.manager;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.OSSObject;
import com.jiang.duckoj.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
public class OOSTest {


    /**
     * oos上传文件
     */
    @Test
    public void testUpload() {

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "examplebucket";
//        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "exampledir/exampleobject.txt";

        String endpoint = FileUtils.END_POINT;
        String accessKeyId = FileUtils.KEY_ID;
        String accessKeySecret = FileUtils.KEY_SECRET;
        String bucketName = FileUtils.BUCKET_NAME;
        String objectName=bucketName+ "/" + "test.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            String content = "Hello OSS";
            //上传文件
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
            System.out.println("上传文件成功");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    /**
     * 读取文件
     */

    @Test
    public void testDownload(){
//        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "examplebucket";
//        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "exampledir/exampleobject.txt";


        String endpoint = FileUtils.END_POINT;
        String accessKeyId = FileUtils.KEY_ID;
        String accessKeySecret = FileUtils.KEY_SECRET;
        String bucketName = FileUtils.BUCKET_NAME;
        String objectName=bucketName+ "/" + "test.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元数据。
            OSSObject ossObject = ossClient.getObject(bucketName, bucketName);
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            if (content != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                while (true) {
                    String line = null;
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (line == null) {
                        break;
                    }
                    System.out.println("\n" + line);
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                try {
                    content.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Test
    public void testDelFile(){

        String endpoint = FileUtils.END_POINT;
        String accessKeyId = FileUtils.KEY_ID;
        String accessKeySecret = FileUtils.KEY_SECRET;
        String bucketName = FileUtils.BUCKET_NAME;
        String objectName=bucketName+ "/" + "test.txt";

        //创建oos实例：
        OSS ossClient  = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 删除文件。
            ossClient.deleteObject(bucketName, objectName);
            System.out.println("删除文件成功");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }


    }

}
