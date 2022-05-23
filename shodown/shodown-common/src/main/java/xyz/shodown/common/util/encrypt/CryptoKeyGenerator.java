package xyz.shodown.common.util.encrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.KeyUtil;
import xyz.shodown.common.enums.EncodingEnum;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.util.UUID;

/**
 * @ClassName: CryptoKeyGenerator
 * @Description: 加密密钥生成工具类
 * @Author: wangxiang
 * @Date: 2021/6/15 16:02
 */
public class CryptoKeyGenerator {

    /**
     * SM2
     */
    private static final String ALGORITHM_SM2 = "SM2";

    /**
     * RSA
     */
    private static final String RSA = "RSA";

    /**
     * 默认密钥长度
     */
    private static final Integer DEFAULT_KEY_SIZE = 1024;

    /**
     * 获取RSA非对称加密公私钥
     */
    public static KeyPair generateRsaKeyPair(){
        return getAsymmetricKeyPair(RSA,DEFAULT_KEY_SIZE);
    }

    /**
     * 获取SM2国密非对称加密公私钥
     */
    public static KeyPair generateSm2KeyPair(){
        return getAsymmetricKeyPair(ALGORITHM_SM2,DEFAULT_KEY_SIZE);
    }

    /**
     * 非对称加密密钥获取
     * @param algorithm 非对称加密算法
     * @param keySize key size
     * @return {@link KeyPair}
     */
    public static KeyPair getAsymmetricKeyPair(String algorithm,int keySize){
        return KeyUtil.generateKeyPair(algorithm,keySize);
    }

    /**
     * 获取字符串私钥,采用base64url_safe编码
     * @param keyPair 密钥对
     * @return 私钥
     */
    public static String strPrivateKey(KeyPair keyPair){
        return strPrivateKey(keyPair,null);
    }

    /**
     * 获取字符串私钥,指定编码格式
     * @param keyPair 密钥对
     * @return 字符串私钥
     */
    public static String strPrivateKey(KeyPair keyPair, EncodingEnum encoding){
        if(encoding==null||encoding== EncodingEnum.BASE64_URL_SAFE){
            return Base64.encodeUrlSafe(keyPair.getPrivate().getEncoded());
        }else if(encoding== EncodingEnum.BASE64){
            return Base64.encode(keyPair.getPrivate().getEncoded());
        }else if(encoding== EncodingEnum.HEX){
            return HexUtil.encodeHexStr(keyPair.getPrivate().getEncoded());
        }else {
            throw new RuntimeException("暂不支持其他编码格式");
        }
    }

    /**
     * 获取字符串公钥,采用base64url_safe编码
     * @param keyPair 密钥对
     * @return 公钥
     */
    public static String strPublicKey(KeyPair keyPair){
        return strPublicKey(keyPair,null);
    }

    /**
     * 获取字符串公钥,指定编码格式
     * @param keyPair 密钥对
     * @return 字符串公钥
     */
    public static String strPublicKey(KeyPair keyPair, EncodingEnum encoding){
        if(encoding==null||encoding== EncodingEnum.BASE64_URL_SAFE){
            return Base64.encodeUrlSafe(keyPair.getPublic().getEncoded());
        }else if(encoding== EncodingEnum.BASE64){
            return Base64.encode(keyPair.getPublic().getEncoded());
        }else if(encoding== EncodingEnum.HEX){
            return HexUtil.encodeHexStr(keyPair.getPublic().getEncoded());
        }else {
            throw new RuntimeException("暂不支持其他编码格式");
        }
    }

    //----------------对称密钥生成-------------------
    /**
     * 产生UUID随机密钥(这里产生密钥必须是16位)
     */
    public static String generateAesSecretKey() {
        String key = UUID.randomUUID().toString();
        // 替换掉-号
        key = key.replace("-", "").substring(0, 16);
        return key;
    }

    /**
     * 随机生成16位长度iv向量
     * @return iv
     */
    public static String generateIv(){
        return generateAesSecretKey();
    }

    /**
     * 完成cipher对象的初始化配置
     * @param cipher cipher
     * @param secretKey 密钥
     * @param iv 向量
     * @param mode 加解密选择
     * @param algorithm 算法
     * @param charset 字符编码
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     */
    public static void initSymmetricCipher(Cipher cipher, String secretKey, String iv, int mode, String algorithm, Charset charset) throws InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] keyBytes = secretKey.getBytes(charset);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes,algorithm);
        byte[] initParam = iv.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        cipher.init(mode,secretKeySpec,ivParameterSpec);
    }

}
