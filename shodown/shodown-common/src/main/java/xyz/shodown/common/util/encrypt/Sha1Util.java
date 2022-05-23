package xyz.shodown.common.util.encrypt;

import cn.hutool.core.util.HexUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: SHA-1加密
 * @author: wangxiang
 * @date: 2022/5/5 09:04
 */
public class Sha1Util {

    /**
     * 通过SHA1进行加密
     * @param content: 待加密内容
     * @param salt：混淆
     * @param iterations hash次数,如果小于1,则默认设置为1次
     * @return 加密结果
     */
    public static String digest(String content, String salt, int iterations) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        if (salt != null) {
            digest.reset();
            digest.update(salt.getBytes());
        }
        byte[] hashed = digest.digest(content.getBytes());
        if(iterations<1){
            iterations = 1;
        }
        for (int i = 1; i < iterations; ++i) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return HexUtil.encodeHexStr(hashed);
    }

    /**
     * 加密字符串内容
     * @param content 待加密内容
     * @param salt 加密混淆
     * @return 加密结果
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String digest(String content,String salt) throws NoSuchAlgorithmException {
        return digest(content,salt,1);
    }

    /**
     * 加密字符串内容
     * @param content 待加密
     * @return 加密结果
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String digest(String content) throws NoSuchAlgorithmException {
        return digest(content,"",1);
    }

}
