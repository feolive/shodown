/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.xhyj.io
 *
 * 版权所有，侵权必究！
 */

package xyz.shodown.dynamicdb.context;

import com.alibaba.druid.pool.DruidDataSource;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.dynamicdb.properties.DruidProperties;
import xyz.shodown.dynamicdb.properties.DynamicDataSourceProperties;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;

/**
 * DruidDataSource
 *
 * @author yangxiaofeng@jsxhyj.cn
 * @since 1.0.0
 */
public class DruidDataSourceFactory {

    /**
     * 构建默认数据源
     * @param dataSourceProperties 默认数据源连接配置
     * @param druidProperties druid配置
     * @return 默认数据源配置
     */
    public static DruidDataSource buildDefaultDatasource(DynamicDataSourceProperties dataSourceProperties, DruidProperties druidProperties) {
        if(druidProperties!=null&& !StringUtil.isBlank(druidProperties.getUrl())
            &&!StringUtil.isBlank(druidProperties.getDriverClassName())){
            return buildDynamicDatasource(druidProperties);
        }
        return getDruidDataSource(druidProperties, dataSourceProperties.getDriverClassName(), dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
    }

    /**
     * 构建动态数据源配置
     * @param properties 动态数据源配置
     * @return 动态数据配置
     */
    public static DruidDataSource buildDynamicDatasource(DruidProperties properties){
        return getDruidDataSource(properties, properties.getDriverClassName(), properties.getUrl(), properties.getUsername(), properties.getPassword());
    }

    @NotNull
    private static DruidDataSource getDruidDataSource(DruidProperties properties, String driverClassName, String url, String username, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        completeDruidDatasourceConf(druidDataSource,properties);
        return druidDataSource;
    }

    /**
     * 补充其余的druid数据源配置
     * @param druidDataSource 已配置的数据源
     * @param properties 剩余待配置的内容
     */
    private static void completeDruidDatasourceConf(DruidDataSource druidDataSource,DruidProperties properties){
        if(properties==null||druidDataSource==null){
            return;
        }
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        druidDataSource.setMaxEvictableIdleTimeMillis(properties.getMaxEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setValidationQueryTimeout(properties.getValidationQueryTimeout());
        druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(properties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
        druidDataSource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
        druidDataSource.setSharePreparedStatements(properties.isSharePreparedStatements());

        try {
            druidDataSource.setFilters(properties.getFilters());
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}