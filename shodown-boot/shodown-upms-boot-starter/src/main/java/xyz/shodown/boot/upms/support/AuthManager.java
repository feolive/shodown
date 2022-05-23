package xyz.shodown.boot.upms.support;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName: AuthManager
 * @Description: 认证管理
 * @Author: wangxiang
 * @Date: 2021-10-21 14:34
 */
@Component
public class AuthManager implements AuthenticationManager {

    @Resource
    private AuthServiceProvider authServiceProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = authServiceProvider.authenticate(authentication);
        if (Objects.nonNull(result)) {
            return result;
        }
        throw new ProviderNotFoundException("认证失败!");
    }

}
