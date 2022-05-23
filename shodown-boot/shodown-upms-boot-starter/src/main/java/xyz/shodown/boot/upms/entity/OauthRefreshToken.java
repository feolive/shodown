package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "oauth_refresh_token")
public class OauthRefreshToken extends BaseEntity {

    @Column(name = "token_id")
    @Comment("token id")
    private String tokenId;

    @Column(name = "token")
    @Comment("token")
    private byte[] token;

    @Column(name = "authentication")
    @Comment("认证处理对象")
    private byte[] authentication;

}