package xyz.shodown.jpa.config;

import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import xyz.shodown.jpa.components.CommentIntegrator;

import java.util.Collections;
import java.util.Map;

/**
 * @description: hibernate配置
 * @author: wangxiang
 * @date: 2022/4/17 15:30
 */
@Configuration
public class HibernateConfig implements HibernatePropertiesCustomizer {

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.use_sql_comments", true);
        hibernateProperties.put("hibernate.integrator_provider",
                (IntegratorProvider) () -> Collections.singletonList(CommentIntegrator.INSTANCE));
    }

}


