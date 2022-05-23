package xyz.shodown.common.util.encrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.common.util.basic.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: AesUtil
 * @Description: AES加解密
 * @Author: wangxiang
 * @Date: 2021/4/15 15:22
 */
@Slf4j
public class AesUtil extends CryptoFileUtil{

    private static final String DEFAULT_SECRET_KEY_AES = "54a52c09dab54cfa";
    private static final SymmetricCrypto AES = new SymmetricCrypto(SymmetricAlgorithm.AES, DEFAULT_SECRET_KEY_AES.getBytes());


    /**
     * 对数据进行aes加密,使用base64url_safe编码字节数组,以及UTF-8编码字符内容
     * @param key aes加密的密钥
     * @param iv 加密的偏移量(必须是16位的字符串)
     * @param data 加密的内容
     * @return 加密数据
     */
    public static String encrypt(String key, String iv,String data) throws Exception{
        byte[] encryptedBytes;
        byte[] byteContent = data.getBytes();
        Cipher cipher = getCipherIns(key, iv, Cipher.ENCRYPT_MODE);
        encryptedBytes = cipher.doFinal(byteContent);
        // 同样对加密后数据进行 base64 编码
        return Base64.encodeUrlSafe(encryptedBytes);
    }

    /**
     * 对数据进行aes加密,指定字节数组编码格式以及字符编码
     * @param secretKey 密钥
     * @param iv 向量
     * @param data 加密数据
     * @param encoding 字节数组编码
     * @param charset 字符编码
     * @return 加密后的字符串
     * @throws Exception 异常
     */
    public static String encrypt(String secretKey, String iv, String data, EncodingEnum encoding, Charset charset)throws Exception{
        byte[] encryptedBytes;
        byte[] byteContent = data.getBytes(charset);
        Cipher cipher = getCipherIns(secretKey, iv, Cipher.ENCRYPT_MODE);
        encryptedBytes = cipher.doFinal(byteContent);
        // 同样对加密后数据进行编码
        return StringUtil.encodeBytesToStr(encryptedBytes,encoding);
    }

    /**
     * 	对数据进行aes解密
     * @param key aes加密的密钥
     * @param content 加密的内容
     * @param iv 加密的偏移量(必须是16位的字符串)
     * @return
     */
    public static String decrypt(String key, String content,String iv) throws Exception{
        // base64 解码
        byte[] encryptedBytes = Base64.decode(content);
        Cipher cipher = getCipherIns(key, iv, Cipher.DECRYPT_MODE);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result, Charsets.UTF8);
    }

    /**
     * 对数据进行aes解密,指定字节数组编码与字符编码
     * @param secretKey 密钥
     * @param iv 向量
     * @param encryptedData 加密字符串
     * @param encoding 字节数组编码格式
     * @param charset 字符编码
     * @return 解密内容
     * @throws Exception 异常
     */
    public static String decrypt(String secretKey, String iv, String encryptedData, EncodingEnum encoding, Charset charset) throws Exception{
        byte[] encryptedBytes;
        if(encoding==null||encoding== EncodingEnum.BASE64_URL_SAFE||encoding== EncodingEnum.BASE64){
            // base64 解码
            encryptedBytes = Base64.decode(encryptedData);
        }else if(encoding== EncodingEnum.HEX){
            encryptedBytes = HexUtil.decodeHex(encryptedData);
        }else {
            throw new RuntimeException("暂不支持其他编码格式");
        }
        Cipher cipher = getCipherIns(secretKey, iv, Cipher.DECRYPT_MODE);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result, charset);
    }

    /**
     * null key 加密 使用默认密钥加密
     * String明文输入,String密文输出
     */
    public static String defaultEncrypt(String str) {
        return Base64.encodeUrlSafe(AES.encrypt(str));
    }

    /**
     * null key 解密 使用默认密钥解密
     * 以String密文输入,String明文输出
     */
    public static String defaultDecrypt(String str) {
        return AES.decryptStr(Base64.decode(str));
    }

    /**
     * 对文件进行加密
     * @param key 密钥
     * @param iv 偏移量
     * @param sourceFile 源文件(明文)
     * @param result 加密后的输出文件
     * @return 加密文件
     * @throws Exception 异常
     */
    public static File encryptFile(String key,String iv,File sourceFile,File result) throws Exception{
        Cipher cipher = getCipherIns(key, iv, Cipher.ENCRYPT_MODE);
        return streamCipherEncryptFile(sourceFile,cipher,result);
    }

    /**
     * 对文件进行解密
     * @param key 密钥
     * @param iv 偏移量
     * @param encryptFile 加密的文件
     * @param result 解密后的输出文件
     * @return 解密文件
     * @throws Exception 异常
     */
    public static File decryptFile(String key,String iv,File encryptFile,File result) throws Exception{
        Cipher cipher = getCipherIns(key, iv, Cipher.DECRYPT_MODE);
        return streamCipherDecryptFile(encryptFile,cipher,result);
    }

    /**
     * 获取cipher实例
     * @param key 密钥
     * @param iv 偏移量
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    private static Cipher getCipherIns(String key,String iv,int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
        IvParameterSpec ivParameterSpec = null;
        if(!StringUtil.isBlank(iv)){
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
            ivParameterSpec = new IvParameterSpec(ivBytes);
        }

        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        if(ivParameterSpec==null){
            cipher.init(mode,secretKey);
        }else {
            cipher.init(mode, secretKey, ivParameterSpec);
        }
        return cipher;
    }

}
