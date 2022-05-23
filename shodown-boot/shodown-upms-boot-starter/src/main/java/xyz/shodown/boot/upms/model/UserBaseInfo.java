package xyz.shodown.boot.upms.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 保存用户信息载体
 * @author: wangxiang
 * @date: 2022/5/5 14:19
 */
@Data
public class UserBaseInfo implements Serializable {

    private String userId;

    private String name;

    private String nickName;

    private String mobile;

    private String email;

    private Integer gender;

    private String token;
}
