package xyz.shodown.crypto.keychain;

import cn.hutool.extra.spring.SpringUtil;

/**
 * @ClassName: DefaultKeyChain
 * @Description: 默认的密钥存储对象,从配置文件中获取
 * @Author: wangxiang
 * @Date: 2021/4/22 10:58
 */
public class DefaultKeyChain extends KeyChain {

    private static final String PRIVATE_KEY = "shodown.crypto.private-key";

    private static final String PUBLIC_KEY = "shodown.crypto.public-key";

    private static final String SECRET_KEY = "shodown.crypto.secret-key";

    private static final String IV = "shodown.crypto.iv";

    @Override
    public String getPrivateKey() {
        return SpringUtil.getProperty(PRIVATE_KEY);
    }

    @Override
    public String getPublicKey() {
        return SpringUtil.getProperty(PUBLIC_KEY);
    }

    @Override
    public String getSecretKey() {
        return SpringUtil.getProperty(SECRET_KEY);
    }

    @Override
    public String getIv() {
        return SpringUtil.getProperty(IV);
    }
}
