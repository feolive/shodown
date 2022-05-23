package xyz.shodown.boot.upms.support;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.common.util.basic.ArrayUtil;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @description: 缓存服务
 * @author: wangxiang
 * @date: 2022/5/8 16:38
 */
@Component
public class CacheProvider {

    private final CaffeineCacheManager caffeineCacheManager;

    private final CaffeineCacheManager tokenCaffeineManager;

    private final RedissonClient redissonClient;

    @Resource
    private AdditionalProperties additionalProperties;

    public CacheProvider(){
        this.caffeineCacheManager = failLoginCaffeineManager();
        this.tokenCaffeineManager = tokenCaffeineCacheManager();
        String[] beanNames = SpringUtil.getBeanNamesForType(RedissonClient.class);
        if(!ArrayUtil.isEmpty(beanNames)){
            this.redissonClient = SpringUtil.getBean(RedissonClient.class);
        }else {
            this.redissonClient = null;
        }
    }

    /**
     * 获取caffeine配置
     * @param cacheName 缓存名称
     * @return 缓存
     */
    public Cache getCaffeineCache(String cacheName){
        return caffeineCacheManager.getCache(cacheName);
    }

    /**
     * token的caffeine缓存配置
     * @param cacheName 缓存名称
     * @return 缓存
     */
    public Cache getTokenCaffeineCache(String cacheName){
        return tokenCaffeineManager.getCache(cacheName);
    }


    /**
     * 获取redisson客户端
     * @return redisson client
     */
    public RedissonClient getRedissonClient(){
        return this.redissonClient;
    }

    /**
     * 失败尝试caffeine
     * @return caffeineCacheManager
     */
    private CaffeineCacheManager failLoginCaffeineManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object,Object> caffeine;
        long afterMinutes = 5L;
        if(additionalProperties!=null){
           long a = additionalProperties.getAccess().getLoginAfterTime();
           if(a>0){
               afterMinutes = a;
           }
        }
        caffeine = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(20000)
                .expireAfterWrite(afterMinutes,TimeUnit.MINUTES);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * 登陆caffeine缓存
     * @return CaffeineCacheManager
     */
    private CaffeineCacheManager tokenCaffeineCacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine<Object,Object> caffeine;
        long afterMinutes = 30L;
        if(additionalProperties!=null){
            long tokenExpiration = additionalProperties.getAccess().getTokenExpiration();
            if(tokenExpiration>0){
                afterMinutes = tokenExpiration;
            }
        }
        caffeine = Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(20000)
                .expireAfterWrite(afterMinutes,TimeUnit.MINUTES);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }


}
