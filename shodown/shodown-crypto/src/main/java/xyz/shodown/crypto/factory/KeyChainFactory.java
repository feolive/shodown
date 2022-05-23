package xyz.shodown.crypto.factory;

import xyz.shodown.crypto.keychain.DefaultKeyChain;
import xyz.shodown.crypto.keychain.GlobalKeyChain;

/**
 * @ClassName: KeyChainFactory
 * @Description: 密钥工厂
 * @Author: wangxiang
 * @Date: 2021/5/25 16:18
 */
public class KeyChainFactory {

    /**
     * 默认密钥
     */
    private static volatile DefaultKeyChain defaultKeyChain;

    /**
     * 全局密钥
     */
    private static volatile GlobalKeyChain globalKeyChain;

    /**
     * 获取默认密钥单例,采用配置文件读取
     * @return DefaultKeyChain
     */
    public static DefaultKeyChain getDefaultKeyChain(){
        if(defaultKeyChain==null){
            synchronized (KeyChainFactory.class){
                if(defaultKeyChain==null){
                    defaultKeyChain = new DefaultKeyChain();
                }
            }
        }
        return defaultKeyChain;
    }

    /**
     * 获取全局密钥单例,用于自定义设置的全局密钥
     * @return GlobalKeyChain
     */
    public static GlobalKeyChain getGlobalKeyChain(){
        if(globalKeyChain==null){
            synchronized (KeyChainFactory.class){
                if(globalKeyChain==null){
                    globalKeyChain = new GlobalKeyChain();
                    DefaultKeyChain defaultKeyChain = getDefaultKeyChain();
                    globalKeyChain.setPrivateKey(defaultKeyChain.getPrivateKey());
                    globalKeyChain.setPublicKey(defaultKeyChain.getPublicKey());
                    globalKeyChain.setSecretKey(defaultKeyChain.getSecretKey());
                    globalKeyChain.setIv(defaultKeyChain.getIv());
                }
            }
        }
        return globalKeyChain;
    }

    /**
     * 获取全局密钥单例,不进行属性初始化
     * @return GlobalKeyChain
     */
    public static GlobalKeyChain getPureGlobalKeyChain(){
        if(globalKeyChain==null){
            synchronized (KeyChainFactory.class){
                if(globalKeyChain==null){
                    globalKeyChain = new GlobalKeyChain();
                }
            }
        }
        return globalKeyChain;
    }
}
