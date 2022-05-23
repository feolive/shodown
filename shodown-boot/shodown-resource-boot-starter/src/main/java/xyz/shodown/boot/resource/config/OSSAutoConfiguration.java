package xyz.shodown.boot.resource.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.shodown.boot.resource.properties.OSSProperties;

/**
 * @Author : caodaohua
 * @Date: 2021/6/9 11:16
 * @Description :
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OSSProperties.class)
@Import({MinioOSSConfig.class, AliyunOSSConfig.class})
public class OSSAutoConfiguration {
}
