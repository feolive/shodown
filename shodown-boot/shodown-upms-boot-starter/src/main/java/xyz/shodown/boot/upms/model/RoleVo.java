package xyz.shodown.boot.upms.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户角色vo
 * @author: wangxiang
 * @date: 2022/4/23 22:50
 */
@Data
public class RoleVo implements Serializable {

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

}
