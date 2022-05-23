package xyz.shodown.boot.cache.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.shodown.boot.cache.properties.CaffeineConfigProps;
import xyz.shodown.boot.cache.properties.RedissonConfigProps;

/**
 * @Author : caodaohua
 * @Date: 2021/6/9 16:46
 * @Description :
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CaffeineConfigProps.class, RedissonConfigProps.class})
@Import({CaffeineCacheConfig.class, RedisCacheConfig.class})
public class CacheConfiguration {

}
