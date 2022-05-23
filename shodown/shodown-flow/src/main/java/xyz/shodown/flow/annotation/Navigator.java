package xyz.shodown.flow.annotation;

import xyz.shodown.flow.direction.DirectionAdapter;
import xyz.shodown.flow.evaluator.EvaluatorAdapter;

import java.lang.annotation.*;

/**
 * @ClassName: Navigator
 * @Description: navigator实例化注解
 * @Author: wangxiang
 * @Date: 2021/6/1 11:17
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Navigator {

    /**
     * 处理完后衔接的direction分支
     */
    Class<? extends DirectionAdapter> direction() default DirectionAdapter.class;

    /**
     * 处理完后衔接的evaluator判断器
     */
    Class<? extends EvaluatorAdapter> evaluator() default EvaluatorAdapter.class;

}
