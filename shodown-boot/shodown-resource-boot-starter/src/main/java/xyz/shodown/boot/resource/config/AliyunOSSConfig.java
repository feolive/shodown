package xyz.shodown.boot.resource.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.shodown.boot.resource.properties.OSSProperties;
import xyz.shodown.boot.resource.service.AliOssService;

/**
 * @Author : caodaohua
 * @Date: 2021/6/9 14:34
 * @Description :
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "shodown.resource.aliyun", name = "active", havingValue = "true")
@EnableConfigurationProperties(OSSProperties.class)
@ConditionalOnClass(OSSClient.class)
public class AliyunOSSConfig {

    @Bean
    @Qualifier("aliOssService")
    AliOssService aliOssService(OSSProperties ossProperties) {
        OSSProperties.Aliyun aliyun = ossProperties.getAliyun();
        if(aliyun==null){
            throw new RuntimeException("missing aliyun essential configuration properties");
        }
        OSSClient ossClient = new OSSClient(aliyun.getEndpoint(),
                aliyun.getAccessKeyId(),
                aliyun.getAccessKeySecret());
        return new AliOssService(ossClient);
    }
}
