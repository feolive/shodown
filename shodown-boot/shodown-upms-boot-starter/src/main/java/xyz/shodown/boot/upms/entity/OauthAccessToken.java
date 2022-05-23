package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "oauth_access_token")
public class OauthAccessToken extends BaseEntity {

    @Column(name = "token_id")
    @Comment("token id")
    private String tokenId;

    @Column(name = "token")
    @Comment("token")
    private byte[] token;

    @Column(name = "authentication_id")
    @Comment("鉴权id")
    private String authenticationId;

    @Column(name = "user_name")
    @Comment("用户名")
    private String userName;

    @Column(name = "client_id")
    @Comment("客户端id")
    private String clientId;

    @Column(name = "authentication")
    @Comment("认证处理对象")
    private byte[] authentication;

    @Column(name = "refresh_token")
    @Comment("刷新token")
    private String refreshToken;

}