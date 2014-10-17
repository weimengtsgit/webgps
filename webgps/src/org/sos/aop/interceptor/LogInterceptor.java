package org.sos.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sos.constant.GlobalConstant;

/**
 * @Title: org.sosgps.aop.interceptor.LogInterceptor.java
 * @Description: equinox
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: yanglei
 * @version 1.0
 * @date: Jun 1, 2008
 */
public class LogInterceptor implements MethodInterceptor {
	private static Log log = LogFactory.getLog(LogInterceptor.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {
		StringBuffer logMsg = new StringBuffer();
		try {
			String className = invocation.getThis().getClass().getName();
			String methodName = invocation.getMethod().getName();
			logMsg.append(GlobalConstant.LOG_PREFIX + " Process: ").append(
					className).append(".").append(methodName).append("() ");
			log.debug(logMsg.toString() + " begin ...");
			Object obj = invocation.proceed();
			log.debug(logMsg.toString() + " end.");
			return obj;
		} catch (Throwable ex) {
			throw ex;
		}
	}

}
