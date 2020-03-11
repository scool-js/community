package com.hyiy.cummunity.provider;

import com.hyiy.cummunity.exception.CustomizeErrorCode;
import com.hyiy.cummunity.exception.CustomizeException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Component
public class TencentCloudProvider {



    public String upload(InputStream inputStream, String fileName, ObjectMetadata objectMetadata ){
        // 指定要上传的文件
        // 指定要上传到 COS 上对象键
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        String generateFileName = "";
        String[] fileSpliter = fileName.split("\\.");
        if(fileSpliter.length>1){
            generateFileName = UUID.randomUUID().toString()+"."+fileSpliter[fileSpliter.length-1];
        }
        else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, generateFileName,inputStream, objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucketName, generateFileName, HttpMethodName.GET);
// 设置下载时返回的 http 头
            Date expirationDate = new Date(System.currentTimeMillis() + 365L*24L*60L * 60L * 1000L);
            req.setExpiration(expirationDate);
            URL url = cosClient.generatePresignedUrl(req);

            return url.toString();
        } catch (CosServiceException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (CosClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }


        // 存储桶的命名格式为 BucketName-APPID


    }
}
