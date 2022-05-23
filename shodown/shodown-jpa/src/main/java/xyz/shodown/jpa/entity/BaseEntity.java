package xyz.shodown.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import xyz.shodown.jpa.annotation.Comment;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description: 基础带自增id的基础实体
 * @author: wangxiang
 * @date: 2022/4/18 15:50
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Comment("自增id")
    private Long id;

}
