package xyz.shodown.boot.upms.keychain;

import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.constants.UpmsConstants;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.model.UserBaseInfo;
import xyz.shodown.boot.upms.repository.ShodownUserRepository;
import xyz.shodown.boot.upms.support.CacheProvider;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.basic.UserInfoUtil;
import xyz.shodown.crypto.properties.CryptoProperties;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @description: 用户动态secret key存储
 * @author: wangxiang
 * @date: 2022/5/11 10:57
 */
@Component
public class DefaultUserSecretKeyStorage extends UserSecretKeyStorage{

    private final ThreadLocal<String> ivStorage = new ThreadLocal<>();

    @Resource
    private CacheProvider cacheProvider;

    @Resource
    private ShodownUserRepository shodownUserRepository;

    @Resource
    private CryptoProperties cryptoProperties;

    @Resource
    private AdditionalProperties additionalProperties;

    @Override
    public void saveUserSecretKey(String userId, String secretKey) {
        if(ShodownUpmsUtil.shouldUseDynamicSecretKey(cryptoProperties,additionalProperties)){
            saveSecretKeyToCache(userId,secretKey);
            shodownUserRepository.saveSecretKey(userId,secretKey);
        }
    }

    @Override
    public String getUserSecretKey(String userId) {
        if(ShodownUpmsUtil.shouldUseDynamicSecretKey(cryptoProperties,additionalProperties)){
           String secretKey = retrieveSecretKey(userId);
           if(StringUtil.isBlank(secretKey)){
               ShodownUser user = shodownUserRepository.findByUserIdAndMark(userId,1);
               if(user==null||StringUtil.isBlank(user.getSecretKey())){
                   return null;
               }else {
                   saveSecretKeyToCache(userId,user.getSecretKey());
                   return user.getSecretKey();
               }
           }else {
               return secretKey;
           }
        }else {
            // 不开启动态获取secret key功能时,则直接取shodown.crypto.secret-key
            return cryptoProperties.getSecretKey();
        }
    }

    @Override
    public void storeIv(@NonNull String iv) {
        ivStorage.set(iv);
    }

    @Override
    public void removeIv() {
        ivStorage.remove();
    }

    @Override
    public String getIv() {
        if(ShodownUpmsUtil.shouldUseDynamicSecretKey(cryptoProperties,additionalProperties)){
            // 动态密钥时,不使用固定的iv
            String iv = ivStorage.get();
            ivStorage.remove();
            return iv;
        }
        return cryptoProperties.getIv();
    }

    private String retrieveSecretKey(String userId){
        RedissonClient redissonClient = cacheProvider.getRedissonClient();
        long expireTime = getExpireTime();
        Object skObj = null;
        if(redissonClient!=null){
             skObj = redissonClient.getBucket(UpmsConstants.DYNAMIC_SECRET_KEY_PREFIX+userId).get();
             if(skObj!=null){
                 redissonClient.getBucket(UpmsConstants.DYNAMIC_SECRET_KEY_PREFIX+userId).expire(Duration.ofMinutes(expireTime));
             }
        }else {
            UserBaseInfo userBaseInfo = UserInfoUtil.getUserInfo(UserBaseInfo.class);
            if(userBaseInfo==null){
                return "";
            }
            Cache cache = cacheProvider.getTokenCaffeineCache(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+userBaseInfo.getToken());
            Cache.ValueWrapper valueWrapper = cache.get(UpmsConstants.DYNAMIC_SECRET_KEY_PREFIX+userId);
            skObj = valueWrapper==null?null:valueWrapper.get();
        }
        return skObj==null?null:(String)skObj;
    }

    /**
     * secret key保存至缓存
     * @param userId 用户id
     * @param secretKey 密钥
     */
    private void saveSecretKeyToCache(String userId,String secretKey){
       RedissonClient redissonClient = cacheProvider.getRedissonClient();
       long expireTime = getExpireTime();
       if(redissonClient!=null){
           redissonClient.getBucket(UpmsConstants.DYNAMIC_SECRET_KEY_PREFIX+userId).set(secretKey,expireTime, TimeUnit.MINUTES);
       }else {
           // 此处共用token的caffeine cache
           UserBaseInfo userBaseInfo = UserInfoUtil.getUserInfo(UserBaseInfo.class);
           if(userBaseInfo==null){
               return;
           }
           Cache cache = cacheProvider.getTokenCaffeineCache(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+userBaseInfo.getToken());
           cache.put(UpmsConstants.DYNAMIC_SECRET_KEY_PREFIX+userId,secretKey);
       }
    }

    /**
     * 获取secret key缓存过期时间
     * @return 过期时间
     */
    private long getExpireTime(){
        if(additionalProperties!=null&&additionalProperties.getAccess().getTokenExpiration()>0){
            return additionalProperties.getAccess().getTokenExpiration() + 1;
        }else {
            return 31;
        }
    }

}
