package xyz.shodown.boot.upms.support;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.constants.UpmsConstants;
import xyz.shodown.boot.upms.entity.ShodownPermission;
import xyz.shodown.boot.upms.entity.ShodownRole;
import xyz.shodown.boot.upms.repository.ShodownPermissionRepository;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.util.basic.ListUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName: DynamicSecurityMetadataSource
 * @Description: 权限数据源
 * @Author: wangxiang
 * @Date: 2021/9/13 10:24
 */
@Component
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private ShodownPermissionRepository shodownPermissionRepository;

    @Resource
    private AdditionalProperties additionalProperties;

    /**
     * 在我们初始化的权限数据中找到对应当前url的权限数据
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        boolean flag = ShodownUpmsUtil.shouldIgnoreUrl(additionalProperties,url);
        if(flag){
            return SecurityConfig.createList(UpmsConstants.ROLE_ANONYMOUS);
        }
        // 获取接口对应的权限配置
        ShodownPermission permission = shodownPermissionRepository.findByRouteAndTypeAndMark(url,2,1);
        if(permission==null){
            return SecurityConfig.createList(UpmsConstants.ROLE_LOGIN);
        }
        List<String> roles = new ArrayList<>();
        List<ShodownRole> roleList = permission.getRoles();
        if(ListUtil.isEmpty(roleList)){
            return null;
        }
        for (ShodownRole shodownRole : roleList) {
            roles.add(shodownRole.getRoleId());
        }
        if(ListUtil.isEmpty(roles)){
            return SecurityConfig.createList(UpmsConstants.ROLE_LOGIN);
        }

        return SecurityConfig.createList(ListUtil.toArray(roles));
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


}
