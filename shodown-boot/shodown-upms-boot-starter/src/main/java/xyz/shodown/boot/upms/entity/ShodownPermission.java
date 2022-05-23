package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import xyz.shodown.common.entity.TreeNode;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.HyperEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "shodown_permission", indexes = {
        @Index(name = "idx_shodownpermission_unq", columnList = "permission_id", unique = true)
})
@Comment("权限表")
public class ShodownPermission extends HyperEntity implements TreeNode<String,ShodownPermission> {

    @Column(name = "permission_id")
    @Comment("权限id")
    private String permissionId;

    @Column(length = 50)
    private String name;

    @Column(name = "val")
    @Comment("权限的值")
    private String val;

    @Column(name = "parent_id",length = 1000)
    @Comment("父级权限id")
    private String parentId;

    @Column(name = "ancients",length = 1000)
    @Comment("祖级机构id,当前机构所有的父级")
    private String ancients;

    @Column(name = "type")
    @Comment("权限类别 0:菜单 1:组件(如按钮,列表等) 2:接口")
    private Integer type;

    @Column(name = "icon", length = 30)
    @Comment("图标名称")
    private String icon;

    @Column(name = "sort")
    @Comment("排序字段")
    private Long sort;

    @Column(name = "component")
    @Comment("前端组件")
    private String component;

    @Column(name = "component_name")
    @Comment("组件名称")
    private String componentName;

    @Column(name = "route")
    @Comment("菜单或接口的地址")
    private String route;

    @Column(name = "redirect_url")
    @Comment("菜单页所需跳转地址")
    private String redirectUrl;

    @Column(name = "menu_level")
    @Comment("菜单层级")
    private Integer menuLevel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="shodown_role_permission",
            joinColumns = {@JoinColumn(name = "permission_id",referencedColumnName = "permission_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")}
    )
    private List<ShodownRole> roles;

    @Transient
    private final List<ShodownPermission> children = new ArrayList<>();

    @Override
    public String fetchSelfId() {
        return this.permissionId;
    }

    @Override
    public String fetchParentId() {
        return this.parentId;
    }

    @Override
    public List<ShodownPermission> fetchChildren() {
        return this.children;
    }

    @Override
    public Long fetchSort() {
        return this.sort;
    }

}