package com.sosgps.wzt.manage.terminalgroup.dao;

import java.util.List;

import com.sosgps.wzt.orm.RefUserTgroup;
import com.sosgps.wzt.orm.RefUserTgroupId;

/**
 * @Title:�û��ɼ��ն���������ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����11:46:51
 */
public interface UserTerminalGroupManageDao {
	public void save(RefUserTgroup transientInstance);

	public void delete(RefUserTgroup persistentInstance);

	public List findByProperty(String propertyName, Object value);

	public void attachDirty(RefUserTgroup instance);
}