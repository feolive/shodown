package xyz.shodown.crypto.keychain;

import org.springframework.beans.factory.InitializingBean;

/**
 * @ClassName: InitKeyChain
 * @Description: KeyChain实例化初始设置
 * @Author: wangxiang
 * @Date: 2021/5/31 15:14
 */
public interface InitKeyChain extends InitializingBean {

    /**
     * 获取私钥/密钥
     * @return 私钥/密钥
     */
    String getPrivateKey();

    /**
     * 获取公钥
     * @return 公钥
     */
    String getPublicKey();

    /**
     * 获取一般密钥
     * @return 密钥
     */
    String getSecretKey();

    /**
     * 获取IV向量
     * @return IV向量
     */
    String getIv();

}
