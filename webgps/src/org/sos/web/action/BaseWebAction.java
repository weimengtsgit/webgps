package org.sos.web.action;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.struts.ActionSupport;

/**
 * @Title: BaseWebAction.java
 * @Description:
 * @Copyright:
 * @Date: 2008-7-15 上午12:41:56
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author IBM
 * @version 1.0
 */
public class BaseWebAction extends ActionSupport {
	private static BeanFactory factory = null;

	private static ApplicationContext appContext = null;

	public Object serviceObj;

	/**
	 * 依赖注入必需的setter 方法
	 */
	public void setServiceObj(Object obj) {
		this.serviceObj = obj;
	}

	/**
	 * 根据beanName获取容器中的bean实例
	 * 
	 * @param beanName
	 *            容器中的bean名
	 * @return 返回Spring 容器中的bean
	 */
	public Object getBean(String beanName) {
		return getWebApplicationContext().getBean(beanName);
	}
}
