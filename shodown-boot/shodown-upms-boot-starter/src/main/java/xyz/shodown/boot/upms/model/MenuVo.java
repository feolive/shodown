package xyz.shodown.boot.upms.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import xyz.shodown.common.entity.TreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 菜单vo
 * @author: wangxiang
 * @date: 2022/4/23 22:54
 */
@Data
public class MenuVo implements TreeNode<String,MenuVo>,Serializable {

    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单路由地址
     */
    private String route;

    /**
     * 跳转链接地址
     */
    private String redirectUrl;

    /**
     * 菜单层级
     */
    private Integer menuLevel;

    /**
     * 父级菜单id
     */
    private String parentId;

    /**
     * 子菜单
     */
    private List<MenuVo> children = new ArrayList<>();

    @JsonIgnore
    private Long sort;

    @JsonIgnore
    @Override
    public String fetchSelfId() {
        return this.menuId;
    }

    @JsonIgnore
    @Override
    public String fetchParentId() {
        return this.parentId;
    }

    @JsonIgnore
    @Override
    public List<MenuVo> fetchChildren() {
        return this.children;
    }

    @JsonIgnore
    @Override
    public Long fetchSort() {
        return this.sort;
    }
}
