package xyz.shodown.dynamicdb.annotation;


import java.lang.annotation.*;

/**
 * 多数据源注解
 *
 * @author yangxiaofeng@jsxhyj.cn
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDB {

    String datasource() default "";
}
