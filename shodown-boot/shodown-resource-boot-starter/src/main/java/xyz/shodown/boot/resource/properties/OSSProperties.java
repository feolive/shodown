package xyz.shodown.boot.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author : caodaohua
 * @Date: 2021/6/9 11:15
 * @Description :
 */
@Data
@ConfigurationProperties(prefix = "shodown.resource")
public class OSSProperties {

    private Aliyun aliyun;
    private Minio minio;


    /**
     * Minio OSS.
     */
    @Data
    public static class Minio {
        // minio service激活标识
        private boolean active;
        // 服务地址
        private String endpoint;
        // minio账户id
        private String accessKey;
        // 账户密码
        private String secretKey;
        // 获取资源url的有效时间,单位秒
        private int urlExpire = 60*60*24;
    }

    /**
     * Aliyun OSS.
     */
    @Data
    public static class Aliyun {
        // 阿里oss激活标识
        private boolean active;
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
    }

}
