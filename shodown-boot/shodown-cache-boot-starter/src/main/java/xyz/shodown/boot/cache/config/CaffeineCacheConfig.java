package xyz.shodown.boot.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.shodown.boot.cache.properties.CaffeineConfigProps;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author : caodaohua
 * @Date: 2021/6/10 9:33
 * @Description :
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cache.caffeine",value = "active", havingValue = "true")
@EnableCaching
public class CaffeineCacheConfig {

    public static final String DEFAULT_CAFFEINE_MANAGER = "default_caffeine_manager";

    @Resource
    private CaffeineConfigProps caffeineConfigProps;

    @Bean(DEFAULT_CAFFEINE_MANAGER)
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object,Object> caffeine = Caffeine.newBuilder()
                .initialCapacity(caffeineConfigProps.getDefaultStrategy().getInitialCapacity())
                .maximumSize(caffeineConfigProps.getDefaultStrategy().getMaximumSize())
                .expireAfterAccess(caffeineConfigProps.getDefaultStrategy().getExpireAfterAccess(), TimeUnit.SECONDS);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

}
