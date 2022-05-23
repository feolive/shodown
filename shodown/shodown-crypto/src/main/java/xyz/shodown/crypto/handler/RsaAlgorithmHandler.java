package xyz.shodown.crypto.handler;

import xyz.shodown.common.util.encrypt.RsaUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @ClassName: RsaAlgorithmHandler
 * @Description: RSA算法处理
 * @Author: wangxiang
 * @Date: 2021/4/20 9:52
 */
public class RsaAlgorithmHandler extends AlgorithmHandlerAdapter{


    @Override
    public String decrypt() throws GeneralSecurityException, IOException {
        return RsaUtil.decrypt(getData(),RsaUtil.getPrivateKey(getKeyChain().getPrivateKey()),getEncoding(),getCharSet().getCharset());
    }

    @Override
    public String encrypt() throws GeneralSecurityException, IOException {
        return RsaUtil.encrypt(getData(),RsaUtil.getPublicKey(getKeyChain().getPublicKey()),getEncoding(),getCharSet().getCharset());
    }

    @Override
    public boolean verify(String sign) throws GeneralSecurityException {
        return RsaUtil.verify(getData(),RsaUtil.getPublicKey(getKeyChain().getPublicKey()),sign,getEncoding(),getCharSet().getCharset());
    }

    @Override
    public String sign() throws GeneralSecurityException {
        return RsaUtil.sign(getData(),RsaUtil.getPrivateKey(getKeyChain().getPrivateKey()),getEncoding(),getCharSet().getCharset());
    }
}
