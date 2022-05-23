package xyz.shodown.flow.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import xyz.shodown.flow.annotation.Direction;
import xyz.shodown.flow.annotation.Evaluator;
import xyz.shodown.flow.annotation.Navigator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @ClassName: AutoConfiguredRegistrar
 * @Description: 自动注册bean
 * @Author: wangxiang
 * @Date: 2021/6/8 19:19
 */
@Slf4j
@Configuration
@Import({AutoConfiguredRegistrar.DirectionAutoConfigRegistrar.class,AutoConfiguredRegistrar.EvaluatorAutoConfigRegistrar.class,AutoConfiguredRegistrar.NavigatorAutoConfigRegistrar.class})
public class AutoConfiguredRegistrar {

    protected static BeanDefinitionBuilder register(BeanFactory beanFactory,Class<? extends Annotation> annotationClass,Class<? extends ScannerConfigurer> definitionClass){
        if (!AutoConfigurationPackages.has(beanFactory)) {
            log.debug("无法确定shodown-flow需要实例化的包路径.");
            return null;
        }
        List<String> packages = AutoConfigurationPackages.get(beanFactory);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(definitionClass);
        builder.addPropertyValue("annotationClass", annotationClass);
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
        return builder;
    }

    public static class DirectionAutoConfigRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar{

        private BeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            BeanDefinitionBuilder builder = AutoConfiguredRegistrar.register(beanFactory, Direction.class,DirectionScannerConfigurer.class);
            if(builder!=null){
                registry.registerBeanDefinition(DirectionScannerConfigurer.class.getName(),builder.getBeanDefinition());
            }
        }
    }

    public static class EvaluatorAutoConfigRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar{
        private BeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            BeanDefinitionBuilder builder = AutoConfiguredRegistrar.register(beanFactory, Evaluator.class,EvaluatorScannerConfigurer.class);
            if(builder!=null){
                registry.registerBeanDefinition(EvaluatorScannerConfigurer.class.getName(),builder.getBeanDefinition());
            }
        }
    }

    public static class NavigatorAutoConfigRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar{

        private BeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            BeanDefinitionBuilder builder = AutoConfiguredRegistrar.register(beanFactory, Navigator.class,NavigatorScannerConfigurer.class);
            if(builder!=null){
                registry.registerBeanDefinition(NavigatorScannerConfigurer.class.getName(),builder.getBeanDefinition());
            }
        }
    }


}
