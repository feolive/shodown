package xyz.shodown.common.util.basic;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.consts.RegexPattern;
import xyz.shodown.common.enums.EncodingEnum;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: Symbols
 * @Description: 字符串工具类, 继承 {@link cn.hutool.core.util.StrUtil StrUtil}类
 * @Author: wangxiang
 * @Date: 2021/2/4 14:09
 */
public class StringUtil extends StrUtil {

    /**
     * 将obj转为字符串,UTF-8编码
     * @param obj obj
     * @return 字符串
     */
    public static String toStr(Object obj){
        return StrUtil.str(obj, Charsets.UTF8);
    }

    /**
     * 将obj转为字符串,指定编码
     * @param obj obj
     * @param charset 编码
     * @return 字符串
     */
    public static String toStr(Object obj, Charset charset){
        return StrUtil.str(obj, charset);
    }

    /**
     * 字节数组编码字符串,注:此处编码并非utf-8等字符编码;
     * 若需使用字符编码,使用{@link StringUtil#toStr(Object, Charset)}
     * 
     * @param bytes 字节数组
     * @param encoding 编码格式
     * @return 编码后的字符串
     */
    public static String encodeBytesToStr(byte[] bytes, EncodingEnum encoding){
        if(encoding==null||encoding== EncodingEnum.BASE64_URL_SAFE){
            return Base64.encodeUrlSafe(bytes);
        }else if(encoding== EncodingEnum.BASE64){
            return Base64.encode(bytes);
        }else if(encoding== EncodingEnum.HEX){
            return HexUtil.encodeHexStr(bytes);
        }else {
            throw new RuntimeException("暂不支持其他编码格式");
        }
    }

    /**
     * 字符串解码字节数组,注:此处编码并非utf-8等字符解码;
     * 若需使用字符解码,使用{@link StringUtil#bytes(CharSequence, Charset)}
     * @param str 字符串
     * @param encoding 编码格式
     * @return 字节数组
     */
    public static byte[] decodeStrToBytes(String str, EncodingEnum encoding){
        if(encoding==null||encoding== EncodingEnum.BASE64_URL_SAFE||encoding== EncodingEnum.BASE64){
            return Base64.decode(str);
        }else if(encoding== EncodingEnum.HEX){
            return HexUtil.decodeHex(str);
        }else {
            throw new RuntimeException("暂不支持其他编码格式");
        }
    }

    /**
     * 判断一个字符串是否都为整数
     * @param strNum 输入
     * @return true是,false不都是整数
     */
    public static boolean isInteger(String strNum) {
        Pattern pattern = Pattern.compile(RegexPattern.INTEGER);
        Matcher matcher = pattern.matcher(strNum);
        return matcher.matches();
    }


}
