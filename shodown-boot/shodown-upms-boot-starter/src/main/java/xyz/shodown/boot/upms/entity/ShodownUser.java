package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.HyperEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "shodown_user", indexes = {
        @Index(name = "idx_shodownuser_user_id_unq", columnList = "user_id", unique = true),
        @Index(name = "idx_shodownuser_token", columnList = "token"),
        @Index(name = "idx_shodownuser_email", columnList = "email"),
        @Index(name = "idx_shodownuser_mobile", columnList = "mobile")
})
@Comment("用户信息表")
public class ShodownUser extends HyperEntity{

    /**
     * 用户id
     */
    @NotBlank
    @Column(name = "user_id", nullable = false, unique = true,length = 50)
    @Comment("用户id")
    private String userId;

    /**
     * 用户名称
     */
    @Column(name = "name",length = 20)
    @Comment("用户姓名")
    private String name;

    @Column(name = "email",length = 100)
    @Comment("邮箱")
    private String email;

    @Column(name = "mobile",length = 20)
    @Comment("手机")
    private String mobile;

    @Column(name = "avatar")
    @Comment("头像")
    private String avatar;

    @Column(name = "gender")
    @Comment("性别 0女 1男")
    private Integer gender = 1;

    @Column(name = "nick_name",length = 20)
    @Comment("昵称")
    private String nickName;

    @Column(name = "note",length = 500)
    @Comment("简介备注")
    private String note;

    @JsonIgnore
    @Column(name = "password")
    @Comment("密码")
    private String password;

    @Column(name = "token")
    @Comment("登陆验证token")
    private String token;

    @Column(name = "salt")
    @Comment("加密盐")
    private String salt;

    @Column(name = "login_time")
    @Comment("上次登陆时间")
    private Date loginTime;

    @Column(name = "secret_key")
    @Comment("数据内容动态加解密钥")
    private String secretKey;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "users")
    private List<ShodownRole> roles;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "users")
    private List<ShodownOrg> orgs;

}