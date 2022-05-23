package xyz.shodown.boot.upms.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 登陆请求报文
 * @author: wangxiang
 * @date: 2022/4/20 10:44
 */
@Data
public class LoginDto implements Serializable {

    /**
     * 登陆用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String validCode;

    /**
     * 短信验证码
     */
    private String msgCode;
}
