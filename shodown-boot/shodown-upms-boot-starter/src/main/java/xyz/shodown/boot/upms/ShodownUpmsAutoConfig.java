package xyz.shodown.boot.upms;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import xyz.shodown.boot.upms.config.AdditionalProperties;

/**
 * @ClassName: ShodownUpmsConfig
 * @Description:
 * @Author: wangxiang
 * @Date: 2021-10-7 22:18
 */
@Configuration
@EnableConfigurationProperties(AdditionalProperties.class)
@ComponentScan
@EnableJpaRepositories(basePackages = "xyz.shodown.boot.upms.repository")
@EntityScan(basePackages = "xyz.shodown.boot.upms.entity")
public class ShodownUpmsAutoConfig {
}
