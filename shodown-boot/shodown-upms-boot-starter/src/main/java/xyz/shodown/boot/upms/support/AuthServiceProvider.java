package xyz.shodown.boot.upms.support;

import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.constants.UpmsConstants;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.model.SecurityUser;
import xyz.shodown.boot.upms.service.UserService;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.date.DateUtil;
import xyz.shodown.common.util.encrypt.MD5Util;
import xyz.shodown.common.util.encrypt.PasswordUtil;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: AuthServiceProvider
 * @Description: 自定义安全认证
 * @Author: wangxiang
 * @Date: 2021/9/16 10:54
 */
@Component
public class AuthServiceProvider implements AuthenticationProvider {

    @Resource
    private UserService userService;

    @Resource
    private CacheProvider cacheProvider;

    @Resource
    private AdditionalProperties additionalProperties;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 认证用户
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if(additionalProperties!=null){
            // 校验登陆次数
            boolean failedCheck = failedLoginTimes(username,additionalProperties.getAccess().getLoginTimeLimit());
            if(failedCheck){
                throw new BadCredentialsException("失败次数过多,请"+additionalProperties.getAccess().getLoginAfterTime()+"分钟后再尝试");
            }
        }
        UserDetails userDetails = userService.loadUserByUsername(username);
        SecurityUser userInfo = userDetails == null ? null : (SecurityUser) userDetails;

        if (userInfo == null) {
            throw new BadCredentialsException("用户不存在");
        }
        ShodownUser user = userInfo.getCurrentUserInfo();
        try {
            boolean res = PasswordUtil.isValidPassword(password, userInfo.getPassword(), userInfo.getCurrentUserInfo().getSalt());
            if (!res) {
                if(additionalProperties!=null){
                    recordFailedTime(username,additionalProperties.getAccess().getLoginAfterTime(),additionalProperties.getAccess().getLoginTimeLimit());
                }
                throw new BadCredentialsException("密码不正确");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new BadCredentialsException("密码所使用的加密算法不存在");
        }
        Boolean isMultiLogin = false;
        if (additionalProperties != null && additionalProperties.getAccess().getMultiLogin() != null) {
            isMultiLogin = additionalProperties.getAccess().getMultiLogin();
        }
        String metaToken = username;
        if (!isMultiLogin) {
            metaToken = username + System.currentTimeMillis();
        }
        String oldToken = user.getToken();
        // 用户信息保存至token
        try {
            user.setToken(PasswordUtil.encodePassword(metaToken,user.getSalt()));
        } catch (NoSuchAlgorithmException e) {
            user.setToken(MD5Util.digest(metaToken,user.getSalt().getBytes(StandardCharsets.UTF_8)));
        }
        user.setLoginTime(DateUtil.date());
        if(!StringUtil.isBlank(oldToken)){
            userService.removeToken(oldToken);
        }
        // 更新用户信息
        userService.updateUser(user);
        BeanCopier beanCopier = BeanCopier.create(ShodownUser.class,ShodownUser.class,false);
        ShodownUser shodownUser = new ShodownUser();
        beanCopier.copy(user,shodownUser,null);
        shodownUser.setPassword(null);
        shodownUser.setSalt(null);
        userInfo.setCurrentUserInfo(shodownUser);
        return new UsernamePasswordAuthenticationToken(userInfo, userInfo.getPassword(), userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    /**
     * 检查失败登陆次数
     * @param username 用户名
     * @param limits 限制的失败尝试次数
     * @return true失败次数超额 false未超额
     */
    private boolean failedLoginTimes(String username,int limits){
        if(limits==0){
            return false;
        }
        RedissonClient redissonClient = cacheProvider.getRedissonClient();
        String cacheName = UpmsConstants.FAILED_LOGIN_TIMES_PREFIX + username;
        Object val;
        if(redissonClient==null){
            Cache cache = cacheProvider.getCaffeineCache(cacheName);
            val = cache.get(username);
        }else{
            val = redissonClient.getBucket(cacheName).get();
        }
        if(val==null){
            return false;
        }else {
            int times = (int) val;
            return times>=limits;
        }
    }

    /**
     * 记录一次失败尝试
     * @param username 用户名
     * @param loginAfterTime 失败尝试后多少分钟才可重新登陆
     */
    private void recordFailedTime(String username,long loginAfterTime,int limits){
        if(loginAfterTime==0||limits==0){
            return;
        }
        RedissonClient redissonClient = cacheProvider.getRedissonClient();
        String cacheName = UpmsConstants.FAILED_LOGIN_TIMES_PREFIX + username;
        if(redissonClient==null){
            Cache cache = cacheProvider.getCaffeineCache(cacheName);
            Object count = cache.get(username);
            if(count==null){
                cache.put(username,1);
            }else {
                int val = (int) count;
                cache.put(username,++val);
            }
        }else {
            Object count = redissonClient.getBucket(cacheName).get();
            if(count==null){
                redissonClient.getBucket(cacheName).set(1,loginAfterTime, TimeUnit.MINUTES);
            }else {
                int val = (int) count;
                redissonClient.getBucket(cacheName).set(++val,loginAfterTime,TimeUnit.MINUTES);
            }
        }
    }
}
