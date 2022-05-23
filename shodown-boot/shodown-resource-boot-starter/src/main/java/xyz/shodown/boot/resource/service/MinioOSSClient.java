package xyz.shodown.boot.resource.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import xyz.shodown.boot.resource.properties.OSSProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class MinioOSSClient {

    private final MinioClient client;

    private final OSSProperties.Minio minioProps;

    public MinioOSSClient(MinioClient minioClient, OSSProperties.Minio minioProps) {
        this.client = minioClient;
        this.minioProps = minioProps;
    }

    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 上传文件,随机名称
     * @param file 文件资源
     * @param bucketName bucket名称
     * @return 上传后的文件名称
     * @throws Exception
     */
    public String uploadRandom(MultipartFile file, String bucketName) throws Exception {
        //文件名
        String originalFilename = file.getOriginalFilename();
        //新的文件名 = 存储桶文件名_时间戳.后缀名
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = bucketName + "_" +
                System.currentTimeMillis() + "_" + format.format(new Date()) + "_" + new Random().nextInt(1000) +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        //开始上传
        uploadInternal(file,bucketName,fileName);
        String url = minioProps.getEndpoint() + "/" + bucketName + "/" + fileName;
        log.info("上传文件成功：[{}]", url);
        return fileName;
    }

    /**
     * 上传文件,覆盖原来同名文件
     * @param file 文件资源
     * @param bucketName bucket名称
     * @return 上传后的文件名称
     */
    public String uploadOverride(MultipartFile file,String bucketName) throws Exception {
        // 文件名
        String originalFilename = file.getOriginalFilename();
        // 开始上传
        uploadInternal(file,bucketName,originalFilename);
        String url = minioProps.getEndpoint() + "/" + bucketName + "/" + originalFilename;
        log.info("上传文件成功：[{}]",url);
        return originalFilename;
    }

    private void uploadInternal(MultipartFile file,String bucketName,String fileName) throws Exception {
        Objects.requireNonNull(fileName);
        //判断文件是否为空
        if (null == file || 0 == file.getSize()) {
            log.warn("文件内容为空,不需要上传");
            return;
        }
        //判断存储桶是否存在  不存在则创建
        createBucket(bucketName);
        putObject(bucketName, fileName, file.getInputStream(),file.getSize(),file.getContentType());
    }
    /**
     * 获取全部bucket
     *
     * @return
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return client.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, ErrorResponseException, ServerException, XmlParserException {
        return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) throws Exception {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 获取⽂件下载链接
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param expires    过期时间,单位秒
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(expires)
                .build();
        return client.getPresignedObjectUrl(args);
    }

    /**
     * 获取文件下载链接
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     * @throws Exception
     */
    public String getObjectURL(String bucketName,String objectName) throws Exception {
        return getObjectURL(bucketName,objectName,minioProps.getUrlExpire());
    }

    /**
     * 获取⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @return ⼆进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return client.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 上传⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param stream     ⽂件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws
            Exception {
        client.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, stream.available(), -1).contentType(objectName.substring(objectName.lastIndexOf("."))).build());
    }

    /**
     * 上传⽂件
     *
     * @param bucketName  bucket名称
     * @param objectName  ⽂件名称
     * @param stream      ⽂件流
     * @param size        ⼤⼩
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long
            size, String contextType) throws Exception {
        client.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, size, -1).contentType(contextType).build());
    }

    /**
     * 获取⽂件信息
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        return client.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 删除⽂件
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @throws Exception https://docs.minio.io/cn/java-client-apireference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }
}
