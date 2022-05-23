package xyz.shodown.crypto.annotation;


import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.enums.CharSet;
import xyz.shodown.crypto.keychain.GlobalKeyChain;
import xyz.shodown.crypto.keychain.KeyChain;

import java.lang.annotation.*;

/**
 * @ClassName: Crypto
 * @Description: 加解密注解
 * @Author: wangxiang
 * @Date: 2021/4/13 15:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crypto {

    /**
     * 是否需要对入参解密,true需要,false不需要
     */
    boolean decrypt() default true;

    /**
     * 是否需要对返回结果加密,true需要,false不需要
     */
    boolean encrypt() default true;

    /**
     * 使用的加密算法
     */
    Algorithm algorithm() default Algorithm.RSA;

    /**
     * 所使用的密钥串
     */
    Class<? extends KeyChain> keyChain() default GlobalKeyChain.class;

    /**
     * 字节数组转字符串的编码格式
     */
    EncodingEnum encoding() default EncodingEnum.BASE64_URL_SAFE;

    /**
     * 字符编码格式
     */
    CharSet charSet() default CharSet.UTF8;

    /**
     * 业务描述,用于记录日志时标记
     */
    String topic() default "";
}
