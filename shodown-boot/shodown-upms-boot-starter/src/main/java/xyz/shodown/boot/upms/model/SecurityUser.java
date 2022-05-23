package xyz.shodown.boot.upms.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.shodown.boot.upms.entity.ShodownRole;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.common.util.basic.ListUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p> 安全认证用户详情 </p>
 *
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * 当前登录用户
     */
    private ShodownUser currentUserInfo;

    /**
     * 角色
     */
    private List<ShodownRole> roleList;


    public SecurityUser() { }

    public SecurityUser(ShodownUser user) {
        if (user != null) {
            this.currentUserInfo = user;
            roleList = filterRoles(user);
        }
    }

    /**
     * 过滤掉mark=0的角色
     * @param user 用户信息
     * @return 有效的角色
     */
    private List<ShodownRole> filterRoles(ShodownUser user){
        List<ShodownRole> rawRoles = user.getRoles();
        if(ListUtil.isEmpty(rawRoles)){
            return null;
        }else {
            List<ShodownRole> result = new ArrayList<>();
            for (ShodownRole rawRole : rawRoles) {
                if(rawRole.getMark()==1){
                    result.add(rawRole);
                }
            }
            return result;
        }
    }

    /**
     * 获取当前用户所具有的角色
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (!ListUtil.isEmpty(this.roleList)) {
            for (ShodownRole role : this.roleList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleId());
                authorities.add(authority);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
