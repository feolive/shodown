package xyz.shodown.boot.resource.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.shodown.boot.resource.properties.OSSProperties;
import xyz.shodown.boot.resource.service.MinioOSSClient;

/**
 * Minio配置类
 *
 * @Author : caodaohua
 * @Date: 2021/3/17 15:59
 * @Description :
 */
@Configuration
@ConditionalOnProperty(prefix = "shodown.resource.minio", name = "active", havingValue = "true")
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(OSSProperties.class)
public class MinioOSSConfig {

    @Bean
    @Qualifier("minioOSSClient")
    public MinioOSSClient minioClient(OSSProperties ossProperties) {

        OSSProperties.Minio minioConfigurationProperties = ossProperties.getMinio();
        if(minioConfigurationProperties==null){
            throw new RuntimeException("missing minio essential configuration properties");
        }

        MinioClient  minioClient = MinioClient.builder()
                .endpoint(minioConfigurationProperties.getEndpoint())
                .credentials(minioConfigurationProperties.getAccessKey(), minioConfigurationProperties.getSecretKey())
                .build();

        return new MinioOSSClient(minioClient,minioConfigurationProperties);
    }


}
