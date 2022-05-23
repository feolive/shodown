package xyz.shodown.boot.cache.constant;

/**
 * @description:
 * @author: wangxiang
 * @date: 2022/5/7 16:26
 */
public interface CacheNames {

    /**
     * 1分钟的有效性
     */
    String TTL_1MIN_IDLE_30SEC = "TTL-1MIN_IDLE-30SEC";

    /**
     * 5分钟有效性
     */
    String TTL_5MIN_IDEL_2MIN = "TTL-5MIN_IDLE-2MIN";

    /**
     * 30分钟有效性
     */
    String TTL_30MIN_IDLE_15MIN = "TTL-30MIN_IDLE-15MIN";

    /**
     * 1天有效性
     */
    String TTL_1DAY_IDLE_30MIN = "TTL-1DAY_IDLE-30MIN";

}
