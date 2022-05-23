package xyz.shodown.flow.annotation;

import xyz.shodown.flow.navigator.NavigatorAdapter;

import java.lang.annotation.*;

/**
 * @ClassName: Evaluator
 * @Description: evaluator实例化注解
 * @Author: wangxiang
 * @Date: 2021/6/1 11:13
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Evaluator {

    /**
     * 判断器eval肯定时,下一步调用的Navigator
     */
    Class<? extends NavigatorAdapter> positive() default NavigatorAdapter.class;

    /**
     * 判断器eval否定时,下一步调用的Navigator
     */
    Class<? extends NavigatorAdapter> negative() default NavigatorAdapter.class;


}
