package xyz.shodown.common.util.encrypt;

import cn.hutool.crypto.digest.MD5;

import java.nio.charset.Charset;

/**
 * @ClassName: MD5Util
 * @Description: MD5不可逆加密
 * @Author: wangxiang
 * @Date: 2021/6/15 16:14
 */
public class MD5Util {

    /**
     * 不指定加盐,盐位置,摘要次数加密
     * @param data 数据
     * @return 16位加密数据
     */
    public static String digest(String data){
        MD5 md5 = new MD5();
        return md5.digestHex16(data);
    }

    /**
     * 指定字符编码,不指定加盐,盐位置,摘要次数加密
     * @param data 数据
     * @param charset 字符编码
     * @return 16位加密数据
     */
    public static String digest(String data, Charset charset){
        MD5 md5 = new MD5();
        return md5.digestHex16(data,charset);
    }

    /**
     * 指定盐,不指定盐位置,摘要次数加密
     * @param data 数据
     * @param salt 盐
     * @return 16位加密数据
     */
    public static String digest(String data,byte[] salt){
        MD5 md5 = new MD5(salt);
        return md5.digestHex16(data);
    }

    /**
     * 指定字符编码,盐,不指定盐位置,摘要次数加密
     * @param data 数据
     * @param salt 盐
     * @param charset 字符编码
     * @return 16位加密数据
     */
    public static String digest(String data,byte[] salt,Charset charset){
        MD5 md5 = new MD5(salt);
        return md5.digestHex16(data,charset);
    }

    /**
     * 指定盐,盐位置,摘要次数加密
     * @param data 数据
     * @param salt 盐
     * @param saltPosition 盐位置,即将盐值字符串放置在数据的index数，默认0
     * @param digestCount 摘要次数，当此值小于等于1,默认为1
     * @return 16位加密数据
     */
    public static String digest(String data,byte[] salt,int saltPosition,int digestCount){
        MD5 md5 = new MD5(salt,saltPosition,digestCount);
        return md5.digestHex16(data);
    }

    /**
     * 指定字符编码,盐,盐位置,摘要次数加密
     * @param data 数据
     * @param salt 盐
     * @param saltPosition 盐位置
     * @param digestCount 摘要次数
     * @param charset 字符编码
     * @return 16位加密数据
     */
    public static String digest(String data,byte[] salt,int saltPosition,int digestCount,Charset charset){
        MD5 md5 = new MD5(salt,saltPosition,digestCount);

        return md5.digestHex16(data,charset);
    }

}
