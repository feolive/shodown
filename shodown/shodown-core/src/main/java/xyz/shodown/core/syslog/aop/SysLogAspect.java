package xyz.shodown.core.syslog.aop;

import cn.hutool.extra.spring.SpringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import xyz.shodown.common.util.basic.ArrayUtil;
import xyz.shodown.common.util.io.HttpUtil;
import xyz.shodown.common.util.io.IpUtil;
import xyz.shodown.common.util.json.JsonUtil;
import xyz.shodown.core.syslog.annotation.SysLog;
import xyz.shodown.core.syslog.content.SysLogContent;
import xyz.shodown.core.syslog.exception.SysLogException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;


/**
 * 操作日志记录
 * @author wangxiang
 */
@Aspect
@Component
public class SysLogAspect {

	SysLogSaverHandler defaultSaver;

	
	@Pointcut("@annotation(xyz.shodown.core.syslog.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) throws Exception {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLogContent content = new SysLogContent();
		SysLog syslogAn = method.getAnnotation(SysLog.class);
		if(syslogAn==null){
			return;
		}
		//注解上的描述
		content.setOperationDesc(syslogAn.value());

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		content.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		String params = JsonUtil.objectToJson(args);
		content.setParams(params);

		//获取request

		HttpServletRequest request = HttpUtil.getHttpServletRequest();
		//设置IP地址
		content.setIp(IpUtil.getIp(request));

		content.setDuration(time);
		content.setCreateDate(new Date());
		// 获取logSaver
		SysLogSaverHandler<?> saver = getLogSaver(syslogAn);

		//保存系统日志
		saver.doSave(content);
	}

	/**
	 * 获取日志记录实现类
	 * @return 实现类
	 */
	private SysLogSaverHandler<?> getLogSaver(SysLog sysLog){
		Map<String,? extends SysLogSaverHandler> map = SpringUtil.getBeansOfType(SysLogSaverHandler.class);

		Class<? extends SysLogSaverHandler> clazz = sysLog.instance();
		if(clazz!=SysLogSaverHandler.class) {
			String[] appointedBeanNames = SpringUtil.getBeanNamesForType(clazz);
			if (!ArrayUtil.isEmpty(appointedBeanNames)) {
				return SpringUtil.getBean(appointedBeanNames[0]);
			}else {
				throw new SysLogException("@SysLog指定的instance实例不存在");
			}
		}
		String[] beanNames = SpringUtil.getBeanNamesForType(SysLogSaverHandler.class);
		if(ArrayUtil.isEmpty(beanNames)){
			throw new SysLogException("未找到SysLogSaver的实现类,请确保至少有一个该接口的实现类");
		}
		if(defaultSaver!=null){
			return defaultSaver;
		}
		for (String beanName : beanNames) {
			// 将isGlobal=true的bean设置为默认的实现类(多个bean设置为global时,只有一个起效),并返回
			SysLogSaverHandler<?> handler = SpringUtil.getBean(beanName);
			if(handler.isGlobal()){
				defaultSaver = handler;
				return defaultSaver;
			}
		}
		// 一个isGlobal=true都未设置时,则使用随机的一个实现类作为默认实现类
		defaultSaver = SpringUtil.getBean(beanNames[0]);
		return defaultSaver;

	}

}
