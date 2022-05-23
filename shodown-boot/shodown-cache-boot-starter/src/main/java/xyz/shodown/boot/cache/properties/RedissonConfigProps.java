package xyz.shodown.boot.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @description: redisson配置
 * @author: wangxiang
 * @date: 2022/5/6 15:56
 */
@Data
@ConfigurationProperties(prefix = "spring.cache.redisson")
public class RedissonConfigProps {

    /**
     * 是否激活redisson缓存
     */
    private boolean active;

    /**
     * redis配置文件名称(目前只支持额外单独配置文件进行配置)
     */
    private String redisConfigFile;

    /**
     * 缓存生命周期配置
     */
    private RedissonCacheLifetime cacheLifetime;

    /**
     * 额外多个缓存生命周期
     */
    private List<RedissonCacheLifetime> extraCacheLifetime;

    @Data
    public static class RedissonCacheLifetime {

        /**
         * 缓存hash配置key名称
         */
        private String cacheName;

        /**
         * 过期时间(秒),默认1分钟;和max-idle-time都设置为0则永久有效
         */
        private long ttl = 60L;

        /**
         * 最长空闲时间(秒),默认30,max-idle-time<=ttl;和ttl都设置为0则永久有效
         */
        private long maxIdleTime = 30L;

        /**
         * 可以缓存的最大数量,默认100;设置为0不限制数量
         */
        private int maxSize = 100;
    }
}
