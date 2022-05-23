package xyz.shodown.boot.upms.support;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.constants.UpmsConstants;
import xyz.shodown.boot.upms.model.SecurityUser;

import java.util.Collection;

/**
 * @ClassName: DynamicAccessDecisionManager
 * @Description: 认证管理器
 * @Author: wangxiang
 * @Date: 2021/9/13 10:30
 */
@Component
public class DynamicAccessDecisionManager implements AccessDecisionManager {


    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 遍历角色
        for (ConfigAttribute ca : configAttributes) {
            // 当前url请求需要的权限
            String needRole = ca.getAttribute();
            if (UpmsConstants.ROLE_LOGIN.equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未登录!");
                } else {
                    return;
                }
            }
            // 可以匿名访问
            if(UpmsConstants.ROLE_ANONYMOUS.equals(needRole)){
                return;
            }

            // 当前用户所具有的角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                // 只要包含其中一个角色即可访问
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("请联系管理员分配权限！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}



