package org.sos.helper;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @Title: SpringHelper.java
 * @Description:
 * @Copyright:
 * @Date: 2008-7-15 ÉÏÎç12:28:59
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author IBM
 * @version 1.0
 */
public class SpringHelper extends HttpServlet {
	private static final long serialVersionUID = -2754866307384407943L;

	private static BeanFactory factory = null;

	private static ApplicationContext appContext = null;

	public static Object getBean(String beanId) {
		return factory.getBean(beanId);
	}

	public static String getMessage(String key) {
		return appContext.getMessage(key, null, Locale.SIMPLIFIED_CHINESE);
	}

	public static String getMessage(String key, Object[] args) {
		return appContext.getMessage(key, args, Locale.SIMPLIFIED_CHINESE);
	}

	public static String getMessage(String key, Locale locale) {
		return appContext.getMessage(key, null, locale);
	}

	public static String getMessage(String key, Object[] args, Locale locale) {
		return appContext.getMessage(key, args, locale);
	}

	public void init() throws ServletException {
		appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		factory = (BeanFactory) appContext;
	}
}
