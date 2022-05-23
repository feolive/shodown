package xyz.shodown.boot.upms.entity;

import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;
import xyz.shodown.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "oauth_approvals")
public class OauthApprovals extends BaseEntity {

    @Column(name = "userId")
    private String userId;

    @Column(name = "clientId")
    private String clientId;

    @Column(name = "scope")
    private String scope;

    @Column(name = "status")
    private String status;

    @Column(name = "expiresAt")
    private Date expiresAt;

    @Column(name = "lastModifiedAt")
    private Date lastModifiedAt;

}