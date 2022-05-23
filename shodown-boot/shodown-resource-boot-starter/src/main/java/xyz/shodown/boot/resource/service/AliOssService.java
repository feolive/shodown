package xyz.shodown.boot.resource.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.InputStream;

/**
 * @Author : caodaohua
 * @Date: 2021/6/9 14:46
 * @Description :
 */
public class AliOssService {

    private final OSSClient ossClient;

    public AliOssService(OSSClient ossClient) {
        this.ossClient = ossClient;
    }

    public void putObject(String bucketName, String objectName, InputStream inputStream, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        this.ossClient.putObject(bucketName, objectName, inputStream, objectMetadata);
    }

    public InputStream getObject(String bucketName, String objectName) {
        return this.ossClient.getObject(bucketName, objectName).getObjectContent();
    }

    public String getObjectUrl(String bucketName, String objectName) {
        return "https://" + bucketName + "." + this.ossClient.getEndpoint() + "/" + objectName;
    }

    public void removeObject(String bucketName, String objectName) throws Exception {
        this.ossClient.deleteObject(bucketName, objectName);
    }
}
