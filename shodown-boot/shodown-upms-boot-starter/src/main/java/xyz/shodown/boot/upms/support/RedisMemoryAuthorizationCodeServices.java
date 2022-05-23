package xyz.shodown.boot.upms.support;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: redis或使用自身内存方案
 * @author: wangxiang
 * @date: 2022/4/21 16:52
 */
public class RedisMemoryAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    protected final ConcurrentHashMap<String, OAuth2Authentication> authorizationCodeStore = new ConcurrentHashMap<String, OAuth2Authentication>();

    private boolean redisExists = false;

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        // todo:判断redis是否有配置，没有则使用自身内存
        if(!redisExists){
            this.authorizationCodeStore.put(code, authentication);
        }else {
            // 使用redis存储
        }

    }

    @Override
    protected OAuth2Authentication remove(String code) {
        // todo:判断redis是否有配置，没有则使用自身内存
        OAuth2Authentication auth;
        if(!redisExists){
            auth = this.authorizationCodeStore.remove(code);
        }else {
            auth = null;
        }

        return auth;
    }
}
