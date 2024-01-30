package com.example.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.FileInputStream;
import java.io.InputStream;

public class AliOss {
    // 在此示例中，使用的是東京地區的端點。
    private static final String ENDPOINT = "oss-ap-northeast-1.aliyuncs.com";
    // 從環境變量中獲取訪問憑證。在運行示例代碼之前，確保已配置OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET環境變量。
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
    private static final String ACCESS_KEY_ID="LTAI5t9kSXVMLVffoQYnU1uP";
    private static final String ACCESS_KEY_SECRET="1LTMomjm9UxGRhDXW5bc7yBBi0CxV3";

    // 指定存儲桶的名稱。
    private static final String BUCKET_NAME = "oscar-zone";
    public static String upload(String objectName, InputStream in) throws Exception {


        // 創建一個OSSClient實例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID,ACCESS_KEY_SECRET);
        String url="";

        try {
            // 指定您要上傳的字符串。
            String content = "Hello OSS, hello world";

            // 創建一個PutObjectRequest對象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, in);

            // 以下示例代碼提供了在上傳對象時如何指定對象的存儲類別和訪問控制列表（ACL）的示例：
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上傳字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //url組成:https://bucket名稱.區域節點/objectName
            url="https://"+BUCKET_NAME+"."+ENDPOINT+"/"+objectName;
        } catch (OSSException oe) {
            System.out.println("捕獲到OSSException，這意味著您的請求已經到達OSS，"
                    + "但由於某些原因而被拒絕並返回錯誤響應。");
            System.out.println("錯誤訊息：" + oe.getErrorMessage());
            System.out.println("錯誤代碼：" + oe.getErrorCode());
            System.out.println("請求ID：" + oe.getRequestId());
            System.out.println("主機ID：" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("捕獲到ClientException，這意味著客戶端在嘗試與OSS通信時遇到了嚴重的內部問題，"
                    + "比如無法訪問網絡。");
            System.out.println("錯誤訊息：" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }

}
