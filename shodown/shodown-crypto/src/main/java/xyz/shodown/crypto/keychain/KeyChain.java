package xyz.shodown.crypto.keychain;

import xyz.shodown.crypto.annotation.KeyRegister;
import xyz.shodown.crypto.enums.CryptoErr;
import xyz.shodown.crypto.exception.ShodownCryptoException;
import xyz.shodown.crypto.factory.KeyChainFactory;

/**
 * @ClassName: KeyChain
 * @Description: 密钥串存储接口
 * @Author: wangxiang
 * @Date: 2021/4/22 10:56
 */
public abstract class KeyChain implements InitKeyChain {


    /**
     * 判断是否是全局的密钥对,并设置全局密钥
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception{
        // 判断global是否全局,全局的话就作为全局密钥使用
        KeyRegister keyRegister = this.getClass().getAnnotation(KeyRegister.class);
        if(keyRegister==null){
            throw new ShodownCryptoException(CryptoErr.WITHOUT_KEY_REGISTER);
        }
        boolean global = keyRegister.global();
        if(global){
            GlobalKeyChain globalKeyChain = KeyChainFactory.getPureGlobalKeyChain();
            if(!globalKeyChain.isInit()){
                globalKeyChain.setPrivateKey(this.getPrivateKey());
                globalKeyChain.setPublicKey(this.getPublicKey());
                globalKeyChain.setSecretKey(this.getSecretKey());
                globalKeyChain.setIv(this.getIv());
                globalKeyChain.setInit(true);
            }
        }
    }

}
