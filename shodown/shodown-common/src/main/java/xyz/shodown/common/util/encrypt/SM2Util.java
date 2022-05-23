package xyz.shodown.common.util.encrypt;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.common.util.basic.StringUtil;

import java.nio.charset.Charset;

/**
 * @ClassName: SM2Util
 * @Description: SM2国密非对称加密工具类
 * @Author: wangxiang
 * @Date: 2021/6/15 15:35
 */
public class SM2Util {

    /**
     * SM2加密
     * @param data 明文数据
     * @param publicKey 公钥
     * @param encoding 字节数组编码
     * @param charset 字符编码
     * @return 编码后的加密字符串
     * @throws Exception 异常
     */
    public static String encrypt(String data, String publicKey, EncodingEnum encoding, Charset charset) throws Exception{
        SM2 sm2 = new SM2(null,publicKey);
        byte[] dataBytes = data.getBytes(charset);
        byte[] resBytes = sm2.encrypt(dataBytes, KeyType.PublicKey);
        return StringUtil.encodeBytesToStr(resBytes,encoding);
    }

    /**
     * SM2解密
     * @param encryptedData 加密数据
     * @param privateKey 私钥
     * @param encoding 字节数组编码
     * @param charset 字符编码
     * @return 解密后的数据
     * @throws Exception 异常
     */
    public static String decrypt(String encryptedData, String privateKey, EncodingEnum encoding, Charset charset) throws Exception{
        byte[] dataBytes = StringUtil.decodeStrToBytes(encryptedData,encoding);
        SM2 sm2 = new SM2(privateKey,null);
        return new String(sm2.decrypt(dataBytes,KeyType.PrivateKey),charset);
    }

    /**
     * 生成签名
     * @param data 明文数据
     * @param privateKey 私钥(字符串格式)
     * @param charset 字符编码
     * @return 签名字节数组
     */
    public static byte[] sign(String data,String privateKey,Charset charset){
        SM2 sm2 = new SM2(privateKey,null);
        return sm2.sign(data.getBytes(charset));
    }

    /**
     * 生成数字签名字符串
     * @param data 明文数组
     * @param privateKey 私钥(字符串格式)
     * @param encoding 字节数组编码
     * @param charset 字符编码
     * @return 对应字节数组编码的字符串输出
     */
    public static String sign(String data, String privateKey, EncodingEnum encoding, Charset charset){
        byte[] signBytes = sign(data,privateKey,charset);
        return StringUtil.encodeBytesToStr(signBytes,encoding);
    }

    /**
     * 验签
     * @param data 明文数据
     * @param publicKey 公钥
     * @param sign 签名
     * @param encoding 字节数组编码
     * @param charset 字符编码
     * @return 验签结果 true验签通过 false验签失败
     */
    public static boolean verify(String data, String publicKey, String sign, EncodingEnum encoding, Charset charset){
        SM2 sm2 = new SM2(null,publicKey);
        byte[] dataBytes = data.getBytes(charset);
        byte[] signBytes = StringUtil.decodeStrToBytes(sign,encoding);
        return sm2.verify(dataBytes,signBytes);
    }

}
