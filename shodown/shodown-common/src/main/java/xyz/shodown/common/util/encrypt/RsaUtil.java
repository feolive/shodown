package xyz.shodown.common.util.encrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.common.util.basic.StringUtil;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @ClassName: RsaUtil
 * @Description: RSA非对称加解密
 * @Author: wangxiang
 * @Date: 2021/4/15 14:51
 */
public class RsaUtil {
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA
     */
    private static final String RSA = "RSA";

    /**
     * MD5withRSA
     */
    private static final String MD5withRSA = "MD5withRSA";

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        byte[] decodedKey = Base64.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        byte[] decodedKey = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密,默认base64url_safe编码
     *
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return 加密内容
     */
    public static String encrypt(String data, PublicKey publicKey) throws GeneralSecurityException, IOException {
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.encodeUrlSafe(encrypt(data.getBytes(),publicKey));
    }

    /**
     * RSA加密,指定编码格式的加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param encoding 编码格式
     * @param charset 字符编码
     * @return 对应编码格式的加密字符串
     * @throws GeneralSecurityException GeneralSecurityException
     * @throws IOException IOException
     */
    public static String encrypt(String data, PublicKey publicKey, EncodingEnum encoding, Charset charset) throws GeneralSecurityException, IOException{
        byte[] encrypts = encrypt(data.getBytes(charset),publicKey);
        return StringUtil.encodeBytesToStr(encrypts,encoding);
    }

    /**
     * 对文件加密
     * @param sourceFile 源文件(明文)
     * @param resultFile 加密后的输出文件
     * @param publicKey 公钥
     * @return 加密文件
     */
    public static File encryptFile(File sourceFile,File resultFile,PublicKey publicKey) throws IOException, GeneralSecurityException {
        byte[] encryptBytes = encrypt(convertToBytes(sourceFile),publicKey);
        try (OutputStream out = new FileOutputStream(resultFile)){
            out.write(encryptBytes);
            out.flush();
            return resultFile;
        }
    }

    /**
     * RSA加密
     * @param dataBytes 待加密的字节数据
     * @param publicKey 公钥
     * @return 加密内容
     */
    public static byte[] encrypt(byte[] dataBytes,PublicKey publicKey) throws GeneralSecurityException, IOException{
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return segmentalHandle(dataBytes, cipher,Cipher.ENCRYPT_MODE);
    }

    /**
     * RSA解密,指定字节编码格式以及字符编码
     *
     * @param data 待解密数据
     * @param privateKey 私钥
     * @param encoding 字节数组编码格式
     * @param charset 字符编码格式
     */
    public static String decrypt(String data, PrivateKey privateKey, EncodingEnum encoding, Charset charset) throws GeneralSecurityException, IOException {
        byte[] dataBytes = StringUtil.decodeStrToBytes(data,encoding);
        // 解密后的内容
        return new String(decrypt(dataBytes,privateKey), charset);
    }

    /**
     * 解密文件
     * @param encryptFile 加密文件
     * @param resultFile 待解密后的输出文件
     * @param privateKey 私钥
     */
    public static File decryptFile(File encryptFile,File resultFile,PrivateKey privateKey) throws IOException, GeneralSecurityException {
        byte[] decryptData = decrypt(convertToBytes(encryptFile),privateKey);
        try (OutputStream out = new FileOutputStream(resultFile)){
            out.write(decryptData);
            out.flush();
            return resultFile;
        }
    }

    /**
     * RSA解密
     * @param dataBytes 待解密字节
     * @param privateKey 私钥
     */
    public static byte[] decrypt(byte[] dataBytes,PrivateKey privateKey) throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return segmentalHandle(dataBytes, cipher,Cipher.DECRYPT_MODE);
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @param charset 字符编码
     * @return 签名
     */
    public static byte[] sign(String data, PrivateKey privateKey,Charset charset) throws GeneralSecurityException{
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(MD5withRSA);
        signature.initSign(key);
        signature.update(data.getBytes(charset));
        return signature.sign();
    }

    /**
     * 生成签名
     * @param data 数据
     * @param privateKey 私钥
     * @param encoding 字节数组编码
     * @param charset 字符编码
     * @return 签名
     * @throws GeneralSecurityException GeneralSecurityException
     */
    public static String sign(String data, PrivateKey privateKey, EncodingEnum encoding, Charset charset) throws GeneralSecurityException{
         byte[] signBytes = sign(data,privateKey,charset);
         return StringUtil.encodeBytesToStr(signBytes,encoding);
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign, EncodingEnum encoding,Charset charset) throws GeneralSecurityException {
        if(encoding==null||encoding== EncodingEnum.BASE64||encoding== EncodingEnum.BASE64_URL_SAFE){
            return verify(srcData.getBytes(charset),publicKey,Base64.decode(sign));
        }else if(encoding== EncodingEnum.HEX){
            return verify(srcData.getBytes(charset),publicKey,HexUtil.decodeHex(sign));
        }else {
            throw new RuntimeException("暂不支持其他编码格式");
        }
    }

    /**
     * 验签
     * @param dataBytes 原始字节
     * @param publicKey 公钥
     * @param signBytes 签名
     * @return 是否验签通过
     */
    public static boolean verify(byte[] dataBytes,PublicKey publicKey,byte[] signBytes) throws GeneralSecurityException{
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(MD5withRSA);
        signature.initVerify(key);
        signature.update(dataBytes);
        return signature.verify(signBytes);
    }

    /**
     * 分段处理
     * @param dataBytes 数据
     * @param cipher cipher
     */
    private static byte[] segmentalHandle(byte[] dataBytes,Cipher cipher,int mode) throws GeneralSecurityException,IOException {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int inputLen = dataBytes.length;
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                int block = MAX_ENCRYPT_BLOCK;
                if(Cipher.DECRYPT_MODE==mode){
                    block = MAX_DECRYPT_BLOCK;
                }
                if (inputLen - offset > block) {
                    cache = cipher.doFinal(dataBytes, offset, block);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * block;
            }
            return out.toByteArray();
        }
    }

    /**
     * 文件转为字节数组
     * @param file 文件
     */
    private static byte[] convertToBytes(File file) throws IOException {
        try(FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream bout = new ByteArrayOutputStream()){
            byte[] tmpbuf = new byte[1024];
            int count = 0;
            while ((count = in.read(tmpbuf)) != -1) {
                bout.write(tmpbuf, 0, count);
                tmpbuf = new byte[1024];
            }
            return bout.toByteArray();
        }
    }
}
