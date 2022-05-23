package xyz.shodown.crypto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: CryptoProperties
 * @Description: 加解密配置
 * @Author: wangxiang
 * @Date: 2021/6/19 16:57
 */
@Data
@ConfigurationProperties(prefix = "shodown.crypto")
public class CryptoProperties {

    /**
     * 私钥(非对称加密使用)
     */
    private String privateKey;

    /**
     * 公钥(非对称加密使用)
     */
    private String publicKey;

    /**
     * 密钥(对称加密使用)
     */
    private String secretKey;

    /**
     * iv向量,必须,长度必须16位(AES加密混淆使用)
     */
    private String iv;

    /**
     * 是否开启加解密,默认关闭 true开启 false关闭
     */
    private boolean switcher=false;

}
