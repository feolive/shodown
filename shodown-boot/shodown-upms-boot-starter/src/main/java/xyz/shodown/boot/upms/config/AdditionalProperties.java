package xyz.shodown.boot.upms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName: YamlProperties
 * @Description:
 * @Author: wangxiang
 * @Date: 2021-11-9 16:06
 */
@Data
@ConfigurationProperties(prefix = "shodown.upms")
public class AdditionalProperties {

    private Access access = new Access();

    private Oauth oauth2;

    @Data
    public static class Access {

        /**
         * 登陆时所需的密钥串,如需在开发时不开启加解密,将shodown.crypto.switcher配置设置为false
         */
        private LoginKeys loginKeys;

        /**
         * 是否开启动态获取AES密钥功能,默认关闭; true开启; false关闭,使用shodown.crypto.secret-key配置
         */
        private boolean dynamicSecretKey=false;

        /**
         * 动态AES密钥开启时,每次产生随机iv的混入加密文本的偏移量,默认1(首部)
         */
        private int ivOffset = 1;

        /**
         * token过期时间(分钟),默认30分钟
         */
        private long tokenExpiration=30;

        /**
         * 限制用户登陆错误次数(次),默认不限制
         */
        private Integer loginTimeLimit = 0;
        /**
         * 错误超过次数后多少分钟后才能继续登录(分钟),默认不限制
         */
        private long loginAfterTime = 0;
        /**
         * 忽略安全认证的URL
         */
        private List<String> ignoreUrls;

        /**
         * 是否允许同一账号多端同时在线
         */
        private Boolean multiLogin;

    }

    @Data
    public static class Oauth{
        /**
         * 重定向地址
         */
        private String redirectUrl;
    }

    @Data
    public static class LoginKeys{
        /**
         * 自己的私钥
         */
        private String privateKey;

        /**
         * 对方的公钥
         */
        private String publicKey;

    }

}
