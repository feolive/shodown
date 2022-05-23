package xyz.shodown.dynamicdb.inject;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.dynamicdb.context.DynamicContextHolder;

/**
 * @ClassName: DataSourceInject
 * @Description: 手动注入动态数据源
 * @Author: wangxiang
 * @Date: 2021/9/16 14:54
 */
@Slf4j(topic = LogCategory.PLATFORM)
public class DataSourceInject {

    /**
     * 动态数据源索引
     * @param dbIndex 动态数据库索引
     */
    public static void inject(String dbIndex){
        DynamicContextHolder.push(dbIndex);
        log.debug("set datasource is {}", dbIndex);
    }

}
