package xyz.shodown.boot.upms.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.model.SecurityUser;

/**
 * @description: 用户业务接口
 * @author: wangxiang
 * @date: 2022/4/23 17:25
 */
public interface UserService extends UserDetailsService {

    /**
     * 更新用户信息
     * @param user 用户
     */
    void updateUser(ShodownUser user);

    /**
     * 根据token查询用户信息
     * @param token 登陆token
     * @return SecurityUser
     */
    SecurityUser getUserByToken(String token);

    /**
     * 删除登陆token
     * @param token 登陆token
     */
    void removeToken(String token);
}
