package xyz.shodown.crypto.handler;

import cn.hutool.extra.spring.SpringUtil;
import xyz.shodown.common.consts.StateConst;
import xyz.shodown.common.util.basic.ArrayUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.CryptoErr;
import xyz.shodown.crypto.exception.ShodownCryptoException;
import xyz.shodown.crypto.factory.KeyChainFactory;
import xyz.shodown.crypto.keychain.GlobalKeyChain;
import xyz.shodown.crypto.keychain.KeyChain;
import xyz.shodown.crypto.properties.CryptoProperties;

import java.security.GeneralSecurityException;

/**
 * @ClassName: AlgorithmHandler
 * @Description: 算法处理接口
 * @Author: wangxiang
 * @Date: 2021/4/20 9:50
 */
public interface AlgorithmHandler {

    /**
     * 解密
     * @return 解密明文
     */
    String decrypt() throws Exception;

    /**
     * 加密
     * @return 加密密文
     */
    String encrypt() throws Exception;

    /**
     * 加载加解密信息
     * @param info 需要加密/解密的内容
     * @return AlgorithmHandler
     */
    AlgorithmHandler load(String info);

    /**
     * 准备加解密的必要配置,如密钥
     * @return AlgorithmHandler
     */
    AlgorithmHandler prepare(Crypto crypto);

    /**
     * 验证签名与内容
     * @return true验证通过,false验证失败
     */
    boolean verify(String sign) throws GeneralSecurityException;

    /**
     * 生成签名
     * @return 签名
     */
    String sign() throws GeneralSecurityException;


    /**
     * 获取密钥串实例
     * @param crypto 注解
     * @return 密钥串
     */
    default KeyChain loadKeyChain(Crypto crypto){
        Class<?> clazz = crypto.keyChain();
        if(clazz== GlobalKeyChain.class){
            return KeyChainFactory.getGlobalKeyChain();
        }
        if(KeyChain.class.isAssignableFrom(clazz)){
            return (KeyChain) SpringUtil.getBean(clazz);
        }else{
            throw new ShodownCryptoException(CryptoErr.NOT_SUB_KEY_CHAIN);
        }
    }

    /**
     * 加解密总开关
     * @return true开启 false关闭
     */
    default boolean fetchSwitch(){
        String[] beanNames = SpringUtil.getBeanNamesForType(CryptoProperties.class);
        if(!ArrayUtil.isEmpty(beanNames)){
            CryptoProperties cryptoProperties = SpringUtil.getBean(CryptoProperties.class);
            return cryptoProperties.isSwitcher();
        }
        String sw = SpringUtil.getProperty("shodown.crypto.switcher");
        if(StringUtil.isBlank(sw)){
            return true;
        }
        boolean f1 = StateConst.TRUE.equalsIgnoreCase(sw.trim());
        boolean f2 = StateConst.FALSE.equalsIgnoreCase(sw.trim());
        if(f1){
            return true;
        }else if(f2){
            return false;
        }else{
            throw new ShodownCryptoException(CryptoErr.SWITCH_VAL_LIMIT);
        }
    }

}
