package xyz.shodown.flow.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import xyz.shodown.common.util.basic.ArrayUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.flow.annotation.Direction;
import xyz.shodown.flow.annotation.Evaluator;
import xyz.shodown.flow.annotation.Navigator;
import xyz.shodown.flow.context.ShodownFlowContext;
import xyz.shodown.flow.direction.DirectionAdapter;
import xyz.shodown.flow.enums.ShodownFlowError;
import xyz.shodown.flow.evaluator.EvaluatorAdapter;
import xyz.shodown.flow.exception.ShodownFlowException;
import xyz.shodown.flow.navigator.NavigatorAdapter;
import xyz.shodown.flow.spring.consts.DirectionProps;
import xyz.shodown.flow.spring.consts.EvaluatorProps;
import xyz.shodown.flow.spring.consts.NavigatorProps;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @ClassName: ClassPathScanner
 * @Description: bean扫描器
 * @Author: wangxiang
 * @Date: 2021/6/8 19:44
 */
@Slf4j
public class ClassPathScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends Annotation> annotationClass;

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public ClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn(ShodownFlowError.NULL_INSTANCE + "!,在'" + Arrays.toString(basePackages)
                    + "'路径下,没有找到对应的[DirectionAdapter/EvaluatorAdapter/NavigatorAdapter]实现类.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            Annotation[] annotations = definition.getBeanClass().getAnnotations();
            handleAnnotations(annotations, definition);
        }
    }

    public void registerFilters() {
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }
    }

    private void handleAnnotations(Annotation[] annotations, BeanDefinition beanDefinition) {
        if (!ArrayUtil.isEmpty(annotations)) {
            String beanClassName = beanDefinition.getBeanClassName();
            String parentName = beanDefinition.getParentName();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Direction.class) {
                    Direction direction = (Direction) annotation;
                    if (StringUtil.isNotEmpty(parentName) && DirectionAdapter.class.getName().equals(parentName)) {
                        Class<? extends EvaluatorAdapter> eval = direction.eval();
                        Class<? extends NavigatorAdapter> nav = direction.nav();
                        boolean entrance = direction.entrance();
                        String evalName = eval.getName();
                        String navName = nav.getName();
                        if(!StringUtil.isBlank(evalName)&&!StringUtil.isBlank(navName)){
                            throw new ShodownFlowException(ShodownFlowError.NEITHER_EVAL_NOR_NAV);
                        }else if(!StringUtil.isBlank(evalName)){
                            beanDefinition.getPropertyValues().add(DirectionProps.EVAL,new RuntimeBeanReference(evalName));
                        }else if(!StringUtil.isBlank(navName)){
                            beanDefinition.getPropertyValues().add(DirectionProps.NAV,new RuntimeBeanReference(navName));
                        }
                        if(entrance){
                            if(ShodownFlowContext.hasSetEntrance()){
                                throw new ShodownFlowException(ShodownFlowError.ALREADY_SET_ENTRANCE_DIRECTION);
                            }else {
                                ShodownFlowContext.setEntranceDirectionName(beanClassName);
                            }
                        }

                    } else {
                        log.warn(ShodownFlowError.NOT_EXPECTED_CLASS+"!,'" + beanClassName + "'不是DirectionAdapter的子类");
                    }
                } else if (annotation.annotationType() == Evaluator.class) {
                    Evaluator evaluator = (Evaluator) annotation;
                    if (StringUtil.isNotEmpty(parentName) && EvaluatorAdapter.class.getName().equals(parentName)) {
                        Class<? extends NavigatorAdapter> positiveName = evaluator.positive();
                        Class<? extends NavigatorAdapter> negativeName = evaluator.negative();
                        beanDefinition.getPropertyValues().add(EvaluatorProps.POSITIVE,new RuntimeBeanReference(positiveName));
                        beanDefinition.getPropertyValues().add(EvaluatorProps.NEGATIVE,new RuntimeBeanReference(negativeName));
                    } else {
                        log.warn(ShodownFlowError.NOT_EXPECTED_CLASS + "!,'" + beanClassName + "'不是EvaluatorAdapter的子类");
                    }
                } else if (annotation.annotationType() == Navigator.class) {
                    Navigator navigator = (Navigator) annotation;
                    if (StringUtil.isNotEmpty(parentName) && NavigatorAdapter.class.getName().equals(parentName)) {
                        Class<? extends DirectionAdapter> directionName = navigator.direction();
                        Class<? extends EvaluatorAdapter> evaluatorName = navigator.evaluator();
                        beanDefinition.getPropertyValues().add(NavigatorProps.DIRECTION,new RuntimeBeanReference(directionName));
                        beanDefinition.getPropertyValues().add(NavigatorProps.EVALUATOR,new RuntimeBeanReference(evaluatorName));
                    } else {
                        log.warn(ShodownFlowError.NOT_EXPECTED_CLASS+"!,'" + beanClassName + "'不是NavigatorAdapter的子类");
                    }
                }
            }
        }
    }
}
