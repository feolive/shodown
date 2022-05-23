package xyz.shodown.boot.upms.support;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.model.SecurityUser;
import xyz.shodown.boot.upms.model.UserBaseInfo;
import xyz.shodown.common.entity.UserInfoDelegate;

/**
 * @description: 获取用户基本信息
 * @author: wangxiang
 * @date: 2022/5/5 14:21
 */
@Component
public class UserInfoProvider extends UserInfoDelegate<UserBaseInfo> {

    @Override
    public UserBaseInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            return null;
        }
        SecurityUser securityUser = authentication.getPrincipal()==null?null:(SecurityUser) authentication.getPrincipal();
        if(securityUser==null){
            return null;
        }
        ShodownUser user = securityUser.getCurrentUserInfo();
        return extractUserBaseInfo(user);
    }

    /**
     * 提取用户基本信息
     * @param user 用户po
     * @return 用户基本信息
     */
    private UserBaseInfo extractUserBaseInfo(ShodownUser user){
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUserId(user.getUserId());
        userBaseInfo.setGender(user.getGender());
        userBaseInfo.setEmail(user.getEmail());
        userBaseInfo.setMobile(user.getMobile());
        userBaseInfo.setName(user.getName());
        userBaseInfo.setNickName(user.getNickName());
        userBaseInfo.setToken(user.getToken());
        return userBaseInfo;
    }
}
