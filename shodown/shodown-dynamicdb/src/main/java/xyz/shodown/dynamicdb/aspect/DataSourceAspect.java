/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.xhyj.io
 *
 * 版权所有，侵权必究！
 */

package xyz.shodown.dynamicdb.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.shodown.dynamicdb.annotation.DynamicDB;
import xyz.shodown.dynamicdb.context.DynamicContextHolder;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 *
 * @author yangxiaofeng@jsxhyj.cn
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(xyz.shodown.dynamicdb.annotation.DynamicDB) " +
            "|| @within(xyz.shodown.dynamicdb.annotation.DynamicDB)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();

        DynamicDB targetDataSource = targetClass.getAnnotation(DynamicDB.class);
        DynamicDB methodDataSource = method.getAnnotation(DynamicDB.class);
        if(targetDataSource != null || methodDataSource != null){
            String value;
            if(methodDataSource != null){
                value = methodDataSource.datasource();
            }else {
                value = targetDataSource.datasource();
            }

            DynamicContextHolder.push(value);
            logger.debug("set datasource is {}", value);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
            logger.debug("clean datasource");
        }
    }
}