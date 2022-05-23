package xyz.shodown.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @description:
 * @author: wangxiang
 * @date: 2022/4/18 15:51
 */
@Getter
@Setter
@MappedSuperclass
public class HyperEntity extends BaseEntity{

    @Column(name = "mark", nullable = false)
    @Comment("有效标识 0无效 1有效")
    private Integer mark = 1;

    @Column(name = "updater")
    @Comment("更新者")
    private String updater;

    @Column(name = "update_time")
    @Comment("更新时间")
    private Date updateTime;

    @Column(name = "creator")
    @Comment("创建者")
    private String creator;

    @Column(name = "create_time")
    @Comment("创建时间")
    private Date createTime;

}
