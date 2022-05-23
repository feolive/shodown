package xyz.shodown.boot.upms.annotation;

import java.lang.annotation.*;

/**
 * 注解于接口方法上,忽略upms全局通用的对称加解密
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreGeneralCrypto {
}
