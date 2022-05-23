package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.HyperEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "shodown_role", indexes = {
        @Index(name = "idx_shodownrole_role_id_unq", columnList = "role_id", unique = true)
})
@Comment("角色表")
public class ShodownRole extends HyperEntity{

    @Column(name = "role_id")
    @Comment("角色id")
    private String roleId;

    @Column(name = "role_name",length = 50)
    @Comment("角色名称")
    private String roleName;

    @Column(name = "sort")
    @Comment("排序字段")
    private Long sort;

    @Column(name = "description")
    @Comment("角色描述")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shodown_user_role",
            joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "user_id")}
    )
    private List<ShodownUser> users;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "roles")
    private List<ShodownPermission> permissions;

}