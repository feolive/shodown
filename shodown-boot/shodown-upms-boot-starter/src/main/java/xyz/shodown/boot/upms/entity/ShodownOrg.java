package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.HyperEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shodown_org", indexes = {
        @Index(name = "idx_shodownorg_org_id_unq", columnList = "org_id", unique = true)
})
@Comment("机构表")
public class ShodownOrg extends HyperEntity {

    @Column(name = "org_id")
    @Comment("机构id")
    private String orgId;

    @Column(name = "org_name")
    @Comment("机构名称")
    private String orgName;

    @Column(name = "parent_id")
    @Comment("父级机构id")
    private String parentId;

    @Column(name = "ancients",length = 1000)
    @Comment("祖级机构id,当前机构所有的父级")
    private String ancients;

    @Column(name = "org_level")
    @Comment("机构层级 1一级机构 2二级机构 ...以此类推")
    private Integer orgLevel;

    @Column(name = "sort")
    @Comment("显示排列顺序")
    private Long sort;

    @Column(name = "note")
    @Comment("备注字段")
    private String note;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shodown_user_org",
            joinColumns = {@JoinColumn(name = "org_id",referencedColumnName = "org_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "user_id")}
    )
    private List<ShodownUser> users;

}