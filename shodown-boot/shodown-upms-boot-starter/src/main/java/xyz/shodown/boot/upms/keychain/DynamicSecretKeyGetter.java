package xyz.shodown.boot.upms.keychain;

/**
 * @description: 获取动态secretKey接口定义
 * @author: wangxiang
 * @date: 2022/5/11 10:49
 */
public interface DynamicSecretKeyGetter {

    /**
     * 获取secretKey
     * @return secretKey
     */
    String getSecretKey();

    /**
     * 获取iv向量
     * @return iv
     */
    String getIv();
}
