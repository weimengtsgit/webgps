package org.sos.dao;

import org.sos.helper.SpringHelper;

/**
 * @Title: DAOFactory.java
 * @Description:
 * @Copyright:
 * @Date: 2008-8-16 ионГ11:49:43
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class DAOFactory {
	public static Object getDAO(String daoId) {
		return SpringHelper.getBean(daoId);
	}

}
