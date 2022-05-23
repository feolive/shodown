package xyz.shodown.boot.upms.service.impl;

import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.constants.UpmsConstants;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.model.SecurityUser;
import xyz.shodown.boot.upms.repository.ShodownUserRepository;
import xyz.shodown.boot.upms.service.UserService;
import xyz.shodown.boot.upms.support.CacheProvider;
import xyz.shodown.common.consts.RegexPattern;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 用户service实现
 * @author: wangxiang
 * @date: 2022/4/23 20:51
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private ShodownUserRepository shodownUserRepository;

    @Resource
    private CacheProvider cacheProvider;

    @Resource
    private AdditionalProperties additionalProperties;

    @Override
    public void updateUser(ShodownUser user) {
        Objects.requireNonNull(user);
        RedissonClient redissonClient = cacheProvider.getRedissonClient();
        BeanCopier beanCopier = BeanCopier.create(ShodownUser.class,ShodownUser.class,false);
        ShodownUser shodownUser = new ShodownUser();
        beanCopier.copy(user,shodownUser,null);
        shodownUser.setPassword(null);
        shodownUser.setSalt(null);
        SecurityUser securityUser = new SecurityUser(shodownUser);
        if(redissonClient==null){
            Cache cache = cacheProvider.getTokenCaffeineCache(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+user.getToken());
            cache.put(user.getToken(),securityUser);
        }else {
            long tokenExpiration = getTokenExpiration();
            redissonClient.getBucket(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+user.getToken()).set(securityUser,tokenExpiration, TimeUnit.MINUTES);
        }

        shodownUserRepository.save(user);
    }

    @Override
    public SecurityUser getUserByToken(String token) {
        RedissonClient redissonClient = cacheProvider.getRedissonClient();
        Object rawSecurityUser;
        if(redissonClient==null){
            // 查询caffeine缓存
            Cache cache = cacheProvider.getTokenCaffeineCache(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+token);
            Cache.ValueWrapper valueWrapper = cache.get(token);
            rawSecurityUser = valueWrapper==null?null:valueWrapper.get();
        }else {
            // 查询redis
            rawSecurityUser = redissonClient.getBucket(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+token).get();
        }
        if(rawSecurityUser==null){
            return null;
        }else{
            return (SecurityUser) rawSecurityUser;
        }
    }

    @Override
    @Transactional
    public void removeToken(String token) {
        RedissonClient redissonClient = cacheProvider.getRedissonClient();
        Object rawSecurityUser;
        if(redissonClient==null){
            Cache cache = cacheProvider.getTokenCaffeineCache(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+token);
            if(cache!=null){
                cache.clear();
                cache=null;
            }
        }else {
            rawSecurityUser = redissonClient.getBucket(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+token).get();
            if(rawSecurityUser!=null){
                redissonClient.getBucket(UpmsConstants.TOKEN_LOGIN_CACHE_PREFIX+token).delete();
            }
        }
        shodownUserRepository.removeToken(token);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ShodownUser user = findLoginUser(username);
        if (user == null) {
            return null;
        } else {
            return new SecurityUser(user);
        }
    }

    /**
     * 根据用户id/手机/邮箱 查找用户
     *
     * @param username 用户id/手机/邮箱
     * @return 用户明细
     */
    private ShodownUser findLoginUser(String username) {
        if (RegexPattern.isRegexMatch(username, RegexPattern.CHINESE_MOBILE_NO)) {
            // 手机号码
            return shodownUserRepository.findByMobileAndMark(username, 1);
        } else if (RegexPattern.isRegexMatch(username, RegexPattern.EMAIL)) {
            // 邮箱
            return shodownUserRepository.findByEmailAndMark(username, 1);
        } else {
            // 其他为用户id方式
            return shodownUserRepository.findByUserIdAndMark(username, 1);
        }
    }

    /**
     * 获取token过期时间
     * @return token过期时间
     */
    private long getTokenExpiration(){
        if(additionalProperties!=null){
            return additionalProperties.getAccess().getTokenExpiration();
        }else {
            return 30L;
        }
    }
}
