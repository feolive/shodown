package xyz.shodown.crypto.handler;

import xyz.shodown.common.util.encrypt.SM2Util;

import java.security.GeneralSecurityException;

/**
 * @ClassName: Sm2AlgorithmHandler
 * @Description: SM2算法处理
 * @Author: wangxiang
 * @Date: 2021/6/16 10:12
 */
public class Sm2AlgorithmHandler extends AlgorithmHandlerAdapter{

    @Override
    public String decrypt() throws Exception {
        return SM2Util.decrypt(getData(),getKeyChain().getPrivateKey(),getEncoding(),getCharSet().getCharset());
    }

    @Override
    public String encrypt() throws Exception {
        return SM2Util.encrypt(getData(),getKeyChain().getPublicKey(),getEncoding(),getCharSet().getCharset());
    }

    @Override
    public boolean verify(String sign) throws GeneralSecurityException {

        return SM2Util.verify(getData(),getKeyChain().getPublicKey(),sign,getEncoding(),getCharSet().getCharset());
    }

    @Override
    public String sign() throws GeneralSecurityException {
        return SM2Util.sign(getData(),getKeyChain().getPrivateKey(),getEncoding(),getCharSet().getCharset());
    }
}
