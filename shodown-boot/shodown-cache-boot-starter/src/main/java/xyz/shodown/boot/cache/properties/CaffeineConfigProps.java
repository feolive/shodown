package xyz.shodown.boot.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: wangxiang
 * @date: 2022/5/6 16:52
 */
@Data
@ConfigurationProperties(prefix = "spring.cache.caffeine")
public class CaffeineConfigProps {

    /**
     * 默认缓存策略,注解使用的缓存策略
     */
    private Strategy defaultStrategy;

    /**
     * 额外缓存策略,用于非注解使用caffeine时使用
     */
    private Map<String,Strategy> extraStrategies;

    @Data
    public static class Strategy{

        /**
         * 最后一次访问后多长时间过期(秒),默认5分钟
         */
        private int expireAfterAccess = 5*60;
        /**
         * 初始缓存长度,默认1
         */
        private int initialCapacity=1;
        /**
         * 最大长度,默认100
         */
        private long maximumSize=100L;
    }
}
