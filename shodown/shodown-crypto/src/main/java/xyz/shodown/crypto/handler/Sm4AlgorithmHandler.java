package xyz.shodown.crypto.handler;

import xyz.shodown.common.util.encrypt.SM4Util;

/**
 * @ClassName: Sm4AlgorithmHandler
 * @Description: SM4国密对称加密处理
 * @Author: wangxiang
 * @Date: 2021/6/17 10:57
 */
public class Sm4AlgorithmHandler extends AlgorithmHandlerAdapter{

    @Override
    public String decrypt() throws Exception {
        String secretKey = getKeyChain().getSecretKey();
        String iv = getKeyChain().getIv();
        return SM4Util.decrypt(secretKey,iv,getData(),getEncoding(),getCharSet().getCharset());
    }

    @Override
    public String encrypt() throws Exception {
        String secretKey = getKeyChain().getSecretKey();
        String iv = getKeyChain().getIv();
        return SM4Util.encrypt(secretKey,iv,getData(),getEncoding(),getCharSet().getCharset());
    }
}
