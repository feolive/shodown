package xyz.shodown.crypto.handler;

import xyz.shodown.common.util.encrypt.AesUtil;

/**
 * @ClassName: AesAlgorithmHandler
 * @Description: AES算法处理
 * @Author: wangxiang
 * @Date: 2021/5/26 9:15
 */
public class AesAlgorithmHandler extends AlgorithmHandlerAdapter{

    @Override
    public String decrypt() throws Exception {
        String key = getKeyChain().getSecretKey();
        String iv = getKeyChain().getIv();
        return AesUtil.decrypt(key,iv,getData(),getEncoding(),getCharSet().getCharset());
    }

    @Override
    public String encrypt() throws Exception {
        String key = getKeyChain().getSecretKey();
        String iv = getKeyChain().getIv();
        return AesUtil.encrypt(key,iv,getData(),getEncoding(),getCharSet().getCharset());
    }

}
