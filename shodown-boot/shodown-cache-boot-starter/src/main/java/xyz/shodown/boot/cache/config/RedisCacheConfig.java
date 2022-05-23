package xyz.shodown.boot.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import xyz.shodown.boot.cache.constant.CacheNames;
import xyz.shodown.boot.cache.properties.RedissonConfigProps;
import xyz.shodown.boot.cache.util.FastJsonCodec;
import xyz.shodown.common.util.basic.ListUtil;
import xyz.shodown.common.util.basic.StringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author : caodaohua
 * @Date: 2021/6/10 9:33
 * @Description :
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cache.redisson", value = "active", havingValue = "true")
@EnableCaching
public class RedisCacheConfig {

    public static final String REDISSON_CACHE_MANAGER = "redisson_cache_manager";

    @Resource
    private RedissonConfigProps redissonConfigProps;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        String configFileName = redissonConfigProps.getRedisConfigFile();
        if (StringUtil.isBlank(configFileName)) {
            throw new RuntimeException("请指定spring.cache.redisson.redis-config-file: 配置文件名称");
        }
        Codec codec = new FastJsonCodec();
        Config config = Config.fromYAML(new ClassPathResource(configFileName).getInputStream());
        if(config.getCodec()==null){
            config.setCodec(codec);
        }
        return Redisson.create(config);
    }

    @Bean(REDISSON_CACHE_MANAGER)
    @Primary
    public CacheManager redissonCacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>(16);
        RedissonConfigProps.RedissonCacheLifetime cacheLifetime = redissonConfigProps.getCacheLifetime();
        if (cacheLifetime != null) {
            CacheConfig cacheConfig = checkCache(cacheLifetime);
            config.put(cacheLifetime.getCacheName(),cacheConfig);
        }
        List<RedissonConfigProps.RedissonCacheLifetime> extras = redissonConfigProps.getExtraCacheLifetime();
        if (!ListUtil.isEmpty(extras)) {
            for (RedissonConfigProps.RedissonCacheLifetime extra : extras) {
                CacheConfig cacheConfig = checkCache(extra);
                config.put(extra.getCacheName(),cacheConfig);
            }
        }

        config.put(CacheNames.TTL_1MIN_IDLE_30SEC, new CacheConfig(TimeUnit.SECONDS.toMillis(60), TimeUnit.SECONDS.toMillis(30)));
        config.put(CacheNames.TTL_5MIN_IDEL_2MIN, new CacheConfig(TimeUnit.SECONDS.toMillis(60 * 5), TimeUnit.SECONDS.toMillis(60 * 2)));
        config.put(CacheNames.TTL_30MIN_IDLE_15MIN, new CacheConfig(TimeUnit.SECONDS.toMillis(60 * 30), TimeUnit.SECONDS.toMillis(60 * 15)));
        config.put(CacheNames.TTL_1DAY_IDLE_30MIN, new CacheConfig(TimeUnit.SECONDS.toMillis(60 * 60 * 24), TimeUnit.SECONDS.toMillis(60 * 30)));
        return new RedissonSpringCacheManager(redissonClient, config);
    }

    /**
     * 判断cache配置是否符合条件
     *
     * @param cache 缓存
     */
    private CacheConfig checkCache(RedissonConfigProps.RedissonCacheLifetime cache) {
        String name = cache.getCacheName();
        if (StringUtil.isBlank(name)) {
            throw new RuntimeException("请指定spring.cache.redisson.extra-cache-lifetime中每个cache-name的值");
        }
        long ttl = cache.getTtl();
        long maxIdleTime = cache.getMaxIdleTime();
        int maxSize = cache.getMaxSize();
        if (maxIdleTime >= 0L && maxIdleTime <= ttl) {
            CacheConfig cc = new CacheConfig(ttl, maxIdleTime);
            if (maxSize >= 0) {
                cc.setMaxSize(maxSize);
            }
            return cc;
        }else {
            throw new RuntimeException("max-idle-time需要小于等于ttl时间");
        }
    }
}
