package org.sos.web.action;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.struts.ActionSupport;

/**
 * @Title: BaseWebAction.java
 * @Description:
 * @Copyright:
 * @Date: 2008-7-15 ����12:41:56
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author IBM
 * @version 1.0
 */
public class BaseWebAction extends ActionSupport {
	private static BeanFactory factory = null;

	private static ApplicationContext appContext = null;

	public Object serviceObj;

	/**
	 * ����ע������setter ����
	 */
	public void setServiceObj(Object obj) {
		this.serviceObj = obj;
	}

	/**
	 * ����beanName��ȡ�����е�beanʵ��
	 * 
	 * @param beanName
	 *            �����е�bean��
	 * @return ����Spring �����е�bean
	 */
	public Object getBean(String beanName) {
		return getWebApplicationContext().getBean(beanName);
	}
}
