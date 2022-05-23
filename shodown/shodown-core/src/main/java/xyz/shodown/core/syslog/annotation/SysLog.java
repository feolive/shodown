/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.xhyj.io
 *
 * 版权所有，侵权必究！
 */

package xyz.shodown.core.syslog.annotation;

import xyz.shodown.core.syslog.aop.SysLogSaverHandler;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author yangxiaofeng@jsxhyj.cn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	/**
	 * 日志说明
	 */
	String value();

	/**
	 * 所使用的日志入库的接口实现类
	 */
	Class<? extends SysLogSaverHandler> instance() default SysLogSaverHandler.class;

}
