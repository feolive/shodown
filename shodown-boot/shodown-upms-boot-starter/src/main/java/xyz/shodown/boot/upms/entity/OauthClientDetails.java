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
@Table(name = "oauth_client_details")
public class OauthClientDetails extends BaseEntity {

    @Column(name = "client_id")
    @Comment("客户端id")
    private String clientId;

    @Column(name = "resource_ids")
    @Comment("对应资源id")
    private String resourceIds;

    @Column(name = "client_secret")
    @Comment("客户端密钥")
    private String clientSecret;

    @Column(name = "scope")
    @Comment("权限范围")
    private String scope;

    @Column(name = "authorized_grant_types")
    @Comment("授权的类型")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    @Comment("前端重定向地址")
    private String webServerRedirectUri;

    @Column(name = "authorities")
    @Comment("授权")
    private String authorities;

    @Column(name = "access_token_validity")
    @Comment("access_token校验")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    @Comment("refresh_token校验")
    private Integer refreshTokenValidity;

    @Column(name = "additional_information",length = 4096)
    @Comment("额外信息")
    private String additionalInformation;

    @Column(name = "autoapprove")
    @Comment("自动授权内容")
    private String autoapprove;

}