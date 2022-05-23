/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.xhyj.io
 *
 * 版权所有，侵权必究！
 */

package xyz.shodown.dynamicdb.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 *
 * @author yangxiaofeng@jsxhyj.cn
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DynamicDataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private Map<String, DruidProperties> dynamic = new LinkedHashMap<>();

}
