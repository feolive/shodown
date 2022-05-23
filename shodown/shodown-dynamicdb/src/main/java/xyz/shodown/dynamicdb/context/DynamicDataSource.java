/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.xhyj.io
 *
 * 版权所有，侵权必究！
 */

package xyz.shodown.dynamicdb.context;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import xyz.shodown.dynamicdb.context.DynamicContextHolder;

/**
 * 多数据源
 *
 * @author yangxiaofeng@jsxhyj.cn
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }

}
