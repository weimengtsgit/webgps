package com.sosgps.wzt.manage.terminalgroup.dao;

import java.util.List;

import com.sosgps.wzt.orm.RefUserTgroup;
import com.sosgps.wzt.orm.RefUserTgroupId;

/**
 * @Title:用户可见终端组管理数据层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 上午11:46:51
 */
public interface UserTerminalGroupManageDao {
	public void save(RefUserTgroup transientInstance);

	public void delete(RefUserTgroup persistentInstance);

	public List findByProperty(String propertyName, Object value);

	public void attachDirty(RefUserTgroup instance);
}