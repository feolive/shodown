package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "shodown_sys_log")
public class ShodownSysLog extends BaseEntity {

    @Column(name = "user_id")
    @Comment("用户身份")
    private String userId;

    @Column(name = "operation_desc")
    @Comment("用户操作")
    private String operationDesc;

    @Column(name = "method")
    @Comment("请求方法")
    private String method;

    @Column(name = "params")
    @Comment("请求参数")
    private String params;

    @Column(name = "duration")
    @Comment("执行时长(毫秒)")
    private Long duration;

    @Column(name = "ip")
    @Comment("IP地址")
    private String ip;

    @Column(name = "create_time")
    @Comment("创建时间")
    private Date createDate;

}