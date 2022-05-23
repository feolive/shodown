package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "oauth_code")
@Comment("授权码表")
public class OauthCode extends BaseEntity {

    @Column(name = "code")
    @Comment("授权码")
    private String code;

    @Column(name = "authentication")
    @Comment("授权信息")
    private byte[] authentication;

}