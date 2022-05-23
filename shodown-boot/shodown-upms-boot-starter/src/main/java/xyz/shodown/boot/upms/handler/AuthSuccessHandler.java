package xyz.shodown.boot.upms.handler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.annotation.IgnoreGeneralCrypto;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.entity.ShodownOrg;
import xyz.shodown.boot.upms.entity.ShodownPermission;
import xyz.shodown.boot.upms.entity.ShodownRole;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.keychain.LoginKeyChain;
import xyz.shodown.boot.upms.keychain.UserSecretKeyStorage;
import xyz.shodown.boot.upms.model.*;
import xyz.shodown.boot.upms.repository.ShodownPermissionRepository;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.consts.HttpConst;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.basic.ListUtil;
import xyz.shodown.common.util.basic.TreeUtil;
import xyz.shodown.common.util.date.DateUtil;
import xyz.shodown.common.util.encrypt.CryptoKeyGenerator;
import xyz.shodown.common.util.io.ResponseUtil;
import xyz.shodown.common.util.json.JsonUtil;
import xyz.shodown.crypto.entity.EncryptRes;
import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.helper.CryptoHelper;
import xyz.shodown.crypto.properties.CryptoProperties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AuthSuccessHandler
 * @Description: 登录成功处理
 * @Author: wangxiang
 * @Date: 2021/9/6 10:22
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private CryptoProperties cryptoProperties;

    @Resource
    private AdditionalProperties additionalProperties;

    @Resource
    private ShodownPermissionRepository shodownPermissionRepository;

    @Resource
    private UserSecretKeyStorage userSecretKeyStorage;

    @SneakyThrows
    @Override
    @Transactional
    @IgnoreGeneralCrypto
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UserInfoVo userInfoVo = wrapReturn(authentication);
        // rsa加密
        String json = encryptProcess(userInfoVo, httpServletResponse);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResponseUtil.out(httpServletResponse, json);
    }

    /**
     * 加密处理
     *
     * @param userInfoVo          用户基础信息
     * @param httpServletResponse {@link HttpServletResponse}
     * @return
     */
    private String encryptProcess(UserInfoVo userInfoVo, HttpServletResponse httpServletResponse) throws Exception {
        if (!ShodownUpmsUtil.shouldCrypto(cryptoProperties)) {
            Result<UserInfoVo> result = Result.success("Login Successfully!", userInfoVo);
            return JsonUtil.objectToJson(result);
        }
        String secretKey = null;
        if (ShodownUpmsUtil.shouldUseDynamicSecretKey(cryptoProperties, additionalProperties)) {
            // 生成对称加密密钥
            secretKey = CryptoKeyGenerator.generateAesSecretKey();
            userInfoVo.setSecretKey(secretKey);
        }
        Result<UserInfoVo> rawData = Result.success("Login Successfully!", userInfoVo);
        String data = JsonUtil.objectToJson(rawData);
        // rsa加密
        CryptoHelper cryptoHelper = new CryptoHelper(Algorithm.RSA, LoginKeyChain.class, "登陆成功:");
        String res = "";
        EncryptRes encryptRes = cryptoHelper.encrypt(data);
        res = encryptRes.getEncryptData();
        httpServletResponse.setHeader(HttpConst.Header.SIGN, encryptRes.getSign());
        userSecretKeyStorage.saveUserSecretKey(userInfoVo.getUserId(), secretKey);
        return res;
    }

    /**
     * 完善返回内容
     *
     * @param auth 认证
     * @return 用户vo
     */
    protected UserInfoVo wrapReturn(Authentication auth) {
        SecurityUser securityUser = ((SecurityUser) auth.getPrincipal());
        ShodownUser user = securityUser.getCurrentUserInfo();
        List<ShodownRole> roleList = securityUser.getRoleList();
        return returnUserInfo(user, roleList);
    }

    /**
     * 构建用户vo
     *
     * @param user     用户信息
     * @param roleList 用户有效角色
     * @return 用户vo
     */
    private UserInfoVo returnUserInfo(ShodownUser user, List<ShodownRole> roleList) {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(user.getUserId());
        userInfoVo.setAvatar(user.getAvatar());
        userInfoVo.setToken(user.getToken());
        userInfoVo.setName(user.getName());
        userInfoVo.setNickName(user.getNickName());
        userInfoVo.setMobile(user.getMobile());
        userInfoVo.setEmail(user.getEmail());
        userInfoVo.setGender(user.getGender());
        userInfoVo.setLoginTime(DateUtil.formatDateTime(user.getLoginTime()));
        userInfoVo.setNote(user.getNote());
        userInfoVo.setComponents(returnComponentVos(roleList));
        userInfoVo.setMenus(returnMenuVos(roleList));
        userInfoVo.setOrgs(returnOrgVos(user));
        userInfoVo.setRoles(returnRoleVos(roleList));
        return userInfoVo;
    }

    /**
     * 用户的角色po转vo
     *
     * @param roles 角色po
     * @return 角色vo
     */
    private List<RoleVo> returnRoleVos(List<ShodownRole> roles) {
        if (ListUtil.isEmpty(roles)) {
            return new ArrayList<>();
        }
        List<RoleVo> roleVos = new ArrayList<>();
        for (ShodownRole role : roles) {
            RoleVo vo = new RoleVo();
            vo.setRoleId(role.getRoleId());
            vo.setRoleName(role.getRoleName());
            roleVos.add(vo);
        }
        return roleVos;
    }

    /**
     * 获取用户菜单vo
     *
     * @param roles 角色po
     * @return 用户菜单
     */
    @Transactional
    protected List<MenuVo> returnMenuVos(List<ShodownRole> roles) {
        if (ListUtil.isEmpty(roles)) {
            return new ArrayList<>();
        }
        List<String> roleIds = new ArrayList<>();
        for (ShodownRole role : roles) {
            roleIds.add(role.getRoleId());
        }
        List<ShodownPermission> rawMenus = shodownPermissionRepository.findAllByRolesInAndTypeAndMark(roleIds, 0, 1);
        if (ListUtil.isEmpty(rawMenus)) {
            return new ArrayList<>();
        }
        List<MenuVo> menus = new ArrayList<>();
        for (ShodownPermission rawMenu : rawMenus) {
            MenuVo menu = new MenuVo();
            menu.setMenuId(rawMenu.getPermissionId());
            menu.setMenuName(rawMenu.getName());
            menu.setMenuLevel(rawMenu.getMenuLevel());
            menu.setIcon(rawMenu.getIcon());
            menu.setParentId(rawMenu.getParentId());
            menu.setRoute(rawMenu.getRoute());
            menu.setRedirectUrl(rawMenu.getRedirectUrl());
            menu.setSort(rawMenu.getSort());
            menus.add(menu);
        }
        return TreeUtil.buildTree(menus, true);
    }

    /**
     * 用户前端组件权限列表
     *
     * @param roles 用户角色
     * @return 组件
     */
    @Transactional
    protected List<ComponentVo> returnComponentVos(List<ShodownRole> roles) {
        if (ListUtil.isEmpty(roles)) {
            return new ArrayList<>();
        }
        List<String> roleIds = new ArrayList<>();
        for (ShodownRole role : roles) {
            roleIds.add(role.getRoleId());
        }
        List<ShodownPermission> rawComponents = shodownPermissionRepository.findAllByRolesInAndTypeAndMark(roleIds, 1, 1);
        if (ListUtil.isEmpty(rawComponents)) {
            return new ArrayList<>();
        }
        List<ComponentVo> componentVos = new ArrayList<>();
        for (ShodownPermission rawComponent : rawComponents) {
            ComponentVo component = new ComponentVo();
            component.setComponentId(rawComponent.getPermissionId());
            component.setComponentName(rawComponent.getName());
            component.setVal(rawComponent.getVal());
            component.setMenuIds(rawComponent.getAncients());
            componentVos.add(component);
        }
        return componentVos;
    }

    /**
     * 用户所属机构
     *
     * @param user
     * @return
     */
    @Transactional
    protected List<OrgVo> returnOrgVos(ShodownUser user) {
        List<ShodownOrg> orgs = user.getOrgs();
        if (ListUtil.isEmpty(orgs)) {
            return new ArrayList<>();
        }
        List<OrgVo> orgVos = new ArrayList<>();
        for (ShodownOrg org : orgs) {
            if (org.getMark() == 1) {
                OrgVo orgVo = new OrgVo();
                orgVo.setOrgId(org.getOrgId());
                orgVo.setOrgName(org.getOrgName());
                orgVo.setOrgLevel(org.getOrgLevel());
                orgVo.setSort(org.getSort());
                orgVo.setParentId(org.getParentId());
                orgVos.add(orgVo);
            }
        }
        return TreeUtil.buildTree(orgVos, true);
    }
}
