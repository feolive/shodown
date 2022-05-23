package xyz.shodown.crypto.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @ClassName: KeyRegister
 * @Description: KeyRegister注解
 * @Author: wangxiang
 * @Date: 2021/4/22 11:16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface KeyRegister {

    /**
     * 是否是全局默认密钥串
     */
    boolean global() default false;

}
