package xyz.shodown.boot.upms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 用户信息vo
 * @author: wangxiang
 * @date: 2022/4/23 22:18
 */
@Data
public class UserInfoVo implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登陆token
     */
    private String token;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 上次登陆时间
     */
    private String loginTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 角色
     */
    private List<RoleVo> roles;

    /**
     * 菜单
     */
    private List<MenuVo> menus;

    /**
     * 有权限的组件
     */
    private List<ComponentVo> components;

    /**
     * 机构
     */
    private List<OrgVo> orgs;

    /**
     * 对称加密密钥
     */
    private String secretKey;

}
