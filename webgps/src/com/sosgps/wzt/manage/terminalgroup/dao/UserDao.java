package com.sosgps.wzt.manage.terminalgroup.dao;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TUser;

/**
 * @Title:�û����ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-30 ����11:04:17
 */
public interface UserDao {
	public Page<TUser> findByPage(final Page<TUser> page, final String hql,
			final Object... values);
}
