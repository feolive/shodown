package xyz.shodown.boot.upms.keychain;

import xyz.shodown.crypto.annotation.KeyRegister;
import xyz.shodown.crypto.keychain.KeyChain;

import javax.annotation.Resource;

/**
 * @description: 动态对称加密密钥串
 * @author: wangxiang
 * @date: 2022/5/11 10:14
 */
@KeyRegister
public class DynamicSecretKeyChain extends KeyChain {

    @Resource
    private DynamicSecretKeyGetter dynamicSecretKeyGetter;

    @Override
    public String getPrivateKey() {
        return null;
    }

    @Override
    public String getPublicKey() {
        return null;
    }

    @Override
    public String getSecretKey() {
        return dynamicSecretKeyGetter.getSecretKey();
    }

    @Override
    public String getIv() {
        return dynamicSecretKeyGetter.getIv();
    }
}
