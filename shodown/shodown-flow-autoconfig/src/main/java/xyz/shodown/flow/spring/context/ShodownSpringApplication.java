package xyz.shodown.flow.spring.context;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @ClassName: ShodownSpringApplication
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/6/11 19:08
 */
public class ShodownSpringApplication extends SpringApplication {

    @Override
    protected ConfigurableApplicationContext createApplicationContext() {
        return new ShodownFlowAnnotationConfigContext();
    }
}
