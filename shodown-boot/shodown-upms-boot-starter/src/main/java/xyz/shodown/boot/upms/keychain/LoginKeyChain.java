package xyz.shodown.boot.upms.keychain;

import cn.hutool.extra.spring.SpringUtil;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.crypto.annotation.KeyRegister;
import xyz.shodown.crypto.keychain.KeyChain;

import javax.annotation.Resource;

/**
 * @description: 登陆时的加解密密钥
 * @author: wangxiang
 * @date: 2022/5/10 15:59
 */
@KeyRegister
public class LoginKeyChain extends KeyChain {

    @Resource
    private AdditionalProperties additionalProperties;

    @Override
    public String getPrivateKey() {
        String res = null;
        if(additionalProperties==null){
            res = SpringUtil.getProperty("shodown.crypto.private-key");
        }else if(additionalProperties.getAccess().getLoginKeys()!=null){
            String pk = additionalProperties.getAccess().getLoginKeys().getPrivateKey();
            if(StringUtil.isBlank(pk)){
                res = SpringUtil.getProperty("shodown.crypto.private-key");
            }else {
                res = pk;
            }
        }
        return res;
    }

    @Override
    public String getPublicKey() {
        String res = null;
        if(additionalProperties==null){
            res = SpringUtil.getProperty("shodown.crypto.public-key");
        }else if(additionalProperties.getAccess().getLoginKeys()!=null){
            String pk = additionalProperties.getAccess().getLoginKeys().getPublicKey();
            if(StringUtil.isBlank(pk)){
                res = SpringUtil.getProperty("shodown.crypto.public-key");
            }else {
                res = pk;
            }
        }
        return res;
    }

    @Override
    public String getSecretKey() {
        return null;
    }

    @Override
    public String getIv() {
        return null;
    }

}
