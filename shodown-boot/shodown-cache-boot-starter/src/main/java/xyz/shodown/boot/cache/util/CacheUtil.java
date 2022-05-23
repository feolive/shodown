package xyz.shodown.boot.cache.util;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.cache.config.CaffeineCacheConfig;
import xyz.shodown.boot.cache.config.RedisCacheConfig;
import xyz.shodown.boot.cache.properties.CaffeineConfigProps;
import xyz.shodown.common.util.basic.MapUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 缓存访问工具类
 * @author: wangxiang
 * @date: 2022/5/8 00:29
 */
@Component
public class CacheUtil {

    @Resource
    private CaffeineConfigProps caffeineConfigProps;

    private static volatile InternalExtraCaffeineMap internalCaffeineMap;

    private static CaffeineConfigProps internalCaffeineConfigs;

    public CacheUtil(){
        internalCaffeineMap = new InternalExtraCaffeineMap();
        internalCaffeineConfigs = caffeineConfigProps;
    }

    /**
     * 获取配置文件中配置的全局caffeine的spring cache缓存
     * @return 缓存
     */
    public static Cache getDefaultCaffeine(String cacheName){
        CaffeineCacheManager cacheManager = SpringUtil.getBean(CaffeineCacheConfig.DEFAULT_CAFFEINE_MANAGER);
        return cacheManager.getCache(cacheName);
    }

    /**
     * 获取自定义的额外caffeine配置策略的spring cache缓存
     * @param extraName 额外配置策略名称
     * @param cacheName 缓存名称
     * @return 缓存
     */
    public static Cache getExtraCaffeine(String extraName,String cacheName){
        CaffeineCacheManager manager = getExtraCaffeineManager(extraName);
        if(manager==null){
            throw new RuntimeException("请确保spring.cache.caffeine.extra-strategies配置有map结构配置");
        }
        return manager.getCache(cacheName);
    }

    /**
     * 获取redisson客户端
     * @return redisson客户端,用于原生redisson操作
     */
    public static RedissonClient getRedissonClient(){
        return SpringUtil.getBean(RedissonClient.class);
    }

    /**
     * 获取redisson的spring cache缓存
     * @param cacheName
     * @return
     */
    public static Cache getRedisson(String cacheName){
        RedissonSpringCacheManager manager = SpringUtil.getBean(RedisCacheConfig.REDISSON_CACHE_MANAGER);
        return manager.getCache(cacheName);
    }

    /**
     * 获取额外的caffeine配置
     * @param extraName 额外配置名称
     * @return CaffeineCacheManager
     */
    private static CaffeineCacheManager getExtraCaffeineManager(String extraName){
        Map<String, CaffeineConfigProps.Strategy> map = internalCaffeineConfigs.getExtraStrategies();
        if(MapUtil.isEmpty(map)){
            return null;
        }else {
            CaffeineCacheManager cacheManager = internalCaffeineMap.get(extraName);
            if(cacheManager==null){
                synchronized (CacheUtil.class){
                    if(cacheManager==null){
                        cacheManager = new CaffeineCacheManager();
                        CaffeineConfigProps.Strategy strategy = map.get(extraName);
                        Caffeine<Object,Object> caffeine = Caffeine.newBuilder()
                                .initialCapacity(strategy.getInitialCapacity())
                                .maximumSize(strategy.getMaximumSize())
                                .expireAfterAccess(strategy.getExpireAfterAccess(), TimeUnit.SECONDS);
                        cacheManager.setCaffeine(caffeine);
                        cacheManager.setAllowNullValues(false);
                        internalCaffeineMap.put(extraName,cacheManager);
                    }
                }
            }
            return cacheManager;
        }
    }

    private static class InternalExtraCaffeineMap{

        private final Map<String, CaffeineCacheManager> map = new HashMap<>();

        /**
         * 保存
         * @param key key
         * @param manager CaffeineCacheManager
         */
        public void put(@NonNull String key, @NonNull CaffeineCacheManager manager){
            map.put(key,manager);
        }

        /**
         * 获取
         * @param key key
         * @return CaffeineCacheManager
         */
        public CaffeineCacheManager get(String key){
            return map.get(key);
        }
    }
}
