package xyz.shodown.crypto;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.shodown.crypto.properties.CryptoProperties;

/**
 * @description:
 * @author: wangxiang
 * @date: 2022/5/10 21:43
 */
@Configuration
@EnableConfigurationProperties(CryptoProperties.class)
@ComponentScan
public class CryptoAutoConfig {
}
