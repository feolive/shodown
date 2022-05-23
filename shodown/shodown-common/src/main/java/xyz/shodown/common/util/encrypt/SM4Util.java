package xyz.shodown.common.util.encrypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.common.util.basic.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

/**
 * @ClassName: SM4Util
 * @Description: SM4国密对称加密算法工具类
 * @Author: wangxiang
 * @Date: 2021/6/16 16:05
 */
public class SM4Util extends CryptoFileUtil{

    /**
     * SM4加密
     * @param secretKey 密钥
     * @param iv 向量
     * @param data 明文数据
     * @param encoding 字节编码
     * @param charset 字符编码
     * @return 加密数据
     * @throws Exception 异常
     */
    public static String encrypt(String secretKey, String iv, String data, EncodingEnum encoding, Charset charset) throws Exception{
        SM4 sm4 = new SM4(SecureUtil.decode(secretKey));
        sm4.setIv(iv.getBytes(charset));
        byte[] encrypts = sm4.encrypt(data,charset);
        return StringUtil.encodeBytesToStr(encrypts,encoding);
    }

    /**
     * 加密文件
     * @param secretKey 密钥
     * @param iv 向量
     * @param sourceFile 待加密文件
     * @param result 输出文件
     * @return 输出文件
     */
    public static File encryptFile(String secretKey,String iv,File sourceFile,File result) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        return encryptFile(secretKey,iv,sourceFile,result,Charsets.UTF8);
    }

    /**
     * 加密文件
     * @param secretKey 密钥
     * @param iv 向量
     * @param sourceFile 待加密文件
     * @param result 输出文件
     * @param charset 字符编码,此处对应secretKey及iv向量的编码
     * @return 输出文件
     * @throws IOException IOException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     */
    public static File encryptFile(String secretKey,String iv,File sourceFile,File result,Charset charset) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
//        Cipher cipher = SecureUtil.createCipher("SM4");
//        CryptoKeyGenerator.initSymmetricCipher(cipher,secretKey,iv,Cipher.ENCRYPT_MODE,"SM4",charset);
//        return streamCipherEncryptFile(sourceFile,cipher,result);
        SM4 sm4 = new SM4(SecureUtil.decode(secretKey));
        sm4.setIv(iv.getBytes(charset));
        byte[] fileBytes = flieToBytes(sourceFile);
        byte[] resBytes = sm4.encrypt(fileBytes);
        return outputFile(resBytes,result);
    }

    /**
     * SM4解密
     * @param secretKey 密钥
     * @param iv 向量
     * @param encryptedData 加密数据
     * @param encoding 字节编码
     * @param charset 字符编码
     * @return 解密数据
     * @throws Exception 异常
     */
    public static String decrypt(String secretKey, String iv, String encryptedData, EncodingEnum encoding, Charset charset) throws Exception{
        SM4 sm4 = new SM4(SecureUtil.decode(secretKey));
        sm4.setIv(iv.getBytes(charset));
        byte[] dataBytes = StringUtil.decodeStrToBytes(encryptedData,encoding);
        return new String(sm4.decrypt(dataBytes),charset);
    }


    /**
     * 文件解密
     * @param secretKey 密钥
     * @param iv 向量
     * @param encryptFile 加密文件
     * @param result 输出文件
     * @param charset 字符编码,此处对应secretKey及iv向量的编码
     * @return 输出文件
     */
    public static File decryptFile(String secretKey,String iv,File encryptFile,File result,Charset charset) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
//        Cipher cipher = SecureUtil.createCipher("SM4");
//        CryptoKeyGenerator.initSymmetricCipher(cipher,secretKey,iv,Cipher.ENCRYPT_MODE,"SM4",charset);
//        return streamCipherDecryptFile(encryptFile,cipher,result);
        SM4 sm4 = new SM4(SecureUtil.decode(secretKey));
        sm4.setIv(iv.getBytes(charset));
        byte[] fileBytes = flieToBytes(encryptFile);
        byte[] resBytes = sm4.decrypt(fileBytes);
        return outputFile(resBytes,result);
    }

    /**
     * 文件解密
     * @param secretKey 密钥
     * @param iv 向量
     * @param encryptFile 加密文件
     * @param result 输出文件
     * @return 输出文件
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws InvalidKeyException InvalidKeyException
     * @throws IOException IOException
     */
    public static File decryptFile(String secretKey,String iv,File encryptFile,File result) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        return decryptFile(secretKey,iv,encryptFile,result,Charsets.UTF8);
    }

}
