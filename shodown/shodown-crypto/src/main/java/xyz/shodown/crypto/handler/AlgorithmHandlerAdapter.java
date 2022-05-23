package xyz.shodown.crypto.handler;

import lombok.Data;
import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.crypto.annotation.Crypto;
import xyz.shodown.crypto.enums.CharSet;
import xyz.shodown.crypto.keychain.KeyChain;

import java.security.GeneralSecurityException;

/**
 * @ClassName: AlgorithmHandlerAdapter
 * @Description: 加密算法适配器
 * @Author: wangxiang
 * @Date: 2021/5/25 10:24
 */
@Data
public abstract class AlgorithmHandlerAdapter implements AlgorithmHandler{
    /**
     * 加密/解密信息
     */
    private String data;

    /**
     * 密钥
     */
    private KeyChain keyChain;

    /**
     * 字节数组编码字符串格式
     */
    private EncodingEnum encoding;

    /**
     * 解码读取时的文字编码格式
     */
    private CharSet charSet;

    @Override
    public AlgorithmHandler load(String info) {
        this.data = info;
        return this;
    }

    @Override
    public AlgorithmHandler prepare(Crypto crypto) {
        this.keyChain = loadKeyChain(crypto);
        this.encoding = crypto.encoding();
        this.charSet = crypto.charSet();
        return this;
    }

    @Override
    public boolean verify(String sign) throws GeneralSecurityException {
        return true;
    }

    @Override
    public String sign() throws GeneralSecurityException {
        return null;
    }

}
