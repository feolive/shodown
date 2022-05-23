/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.xhyj.io
 *
 * 版权所有，侵权必究！
 */

package xyz.shodown.dynamicdb.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.util.basic.MapUtil;
import xyz.shodown.dynamicdb.aspect.DataSourceAspect;
import xyz.shodown.dynamicdb.context.DruidDataSourceFactory;
import xyz.shodown.dynamicdb.context.DynamicDataSource;
import xyz.shodown.dynamicdb.properties.DruidProperties;
import xyz.shodown.dynamicdb.properties.DynamicDataSourceProperties;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置多数据源
 *
 * @author yangxiaofeng@jsxhyj.cn
 */
@Slf4j(topic = LogCategory.PLATFORM)
@Configuration
@ConditionalOnProperty(prefix = "spring.datasource",name = "type", havingValue = "com.alibaba.druid.pool.DruidDataSource")
@EnableConfigurationProperties({DynamicDataSourceProperties.class, DruidProperties.class})
@Import(DataSourceAspect.class)
public class DynamicDataSourceConfig {

    /**
     * 动态数据源配置
     */
    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    /**
     * 注入主数据源配置,目前支持druid
     */
    @Resource
    private DruidProperties druidProperties;

    /**
     * 注册数据库配置到全局
     * @return 动态数据源配置
     */
    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(getDynamicDataSource());

        //默认数据源
        DruidDataSource defaultDataSource = DruidDataSourceFactory.buildDefaultDatasource(dynamicDataSourceProperties,druidProperties);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    /**
     * 构建动态数据源配置
     * @return 动态数据源配置
     */
    private Map<Object, Object> getDynamicDataSource(){
        Map<String, DruidProperties> dataSourcePropertiesMap = dynamicDataSourceProperties.getDynamic();
        if(MapUtil.isEmpty(dataSourcePropertiesMap)){
            log.debug("未配置动态数据源");
            return new HashMap<>();
        }
        Map<Object, Object> targetDataSources = new HashMap<>(dataSourcePropertiesMap.size());
        dataSourcePropertiesMap.forEach((k, v) -> {
            DruidDataSource druidDataSource = DruidDataSourceFactory.buildDynamicDatasource(v);
            targetDataSources.put(k, druidDataSource);
        });

        return targetDataSources;
    }

}