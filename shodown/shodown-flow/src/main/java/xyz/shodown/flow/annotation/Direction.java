package xyz.shodown.flow.annotation;

import xyz.shodown.flow.evaluator.EvaluatorAdapter;
import xyz.shodown.flow.navigator.NavigatorAdapter;

import java.lang.annotation.*;

/**
 * @ClassName: Direction
 * @Description: direction实例注解
 * @Author: wangxiang
 * @Date: 2021/6/1 11:09
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Direction {

    /**
     * 入口evaluator类
     */
    Class<? extends EvaluatorAdapter> eval() default EvaluatorAdapter.class;

    Class<? extends NavigatorAdapter> nav() default NavigatorAdapter.class;

    /**
     * 是否是起始Direction
     */
    boolean entrance() default false;

}
