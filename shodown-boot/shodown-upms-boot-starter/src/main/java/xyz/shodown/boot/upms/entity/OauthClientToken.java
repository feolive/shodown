package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "oauth_client_token")
public class OauthClientToken extends BaseEntity {

    @Column(name = "token_id")
    @Comment("token id")
    private String tokenId;

    @Column(name = "token")
    @Comment("token")
    private byte[] token;

    @Column(name = "authentication_id")
    @Comment("认证id")
    private String authenticationId;

    @Column(name = "user_name")
    @Comment("用户名")
    private String userName;

    @Column(name = "clientId")
    @Comment("客户端id")
    private String clientId;
}