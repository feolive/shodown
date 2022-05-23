package xyz.shodown.common.util.encrypt;

import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  <p> 加密工具 </p>
 */
@Slf4j
public class PasswordUtil {

    /**
     * 校验密码是否一致
     * @param password: 前端传过来的加密密码
     * @param hashedPassword：数据库中储存加密过后的密码
     * @param salt：盐值
     * @return
     */
    public static boolean isValidPassword(String password, String hashedPassword, String salt) throws NoSuchAlgorithmException {
        return hashedPassword.equalsIgnoreCase(encodePassword(password, salt));
    }

    /**
     * 通过SHA1对密码进行编码
     * @param password：密码
     * @param salt：盐值
     * @param iterations hash次数,如果小于1,则默认设置为1次
     * @return 加密结果
     */
    public static String encodePassword(String password, String salt, int iterations) throws NoSuchAlgorithmException {
        return Sha1Util.digest(password,salt,iterations);
    }

    /**
     * SHA1算法一次hash进行编码
     * @param password 密码
     * @param salt 盐
     * @return 加密结果
     * @throws NoSuchAlgorithmException
     */
    public static String encodePassword(String password, String salt) throws NoSuchAlgorithmException {
        return encodePassword(password,salt,1);
    }

    /**
     * sha1算法加密
     * @param password 密码
     * @return 加密结果
     * @throws NoSuchAlgorithmException
     */
    public static String encodePassword(String password) throws NoSuchAlgorithmException {
        return encodePassword(password,"",1);
    }

}
