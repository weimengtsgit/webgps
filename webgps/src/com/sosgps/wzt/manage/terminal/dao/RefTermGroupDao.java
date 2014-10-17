package com.sosgps.wzt.manage.terminal.dao;

import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.RefTermGroupId;
/**
 * @Title:�ն������ն������ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-30 ����02:51:46
 */
public interface RefTermGroupDao {
	public void save(RefTermGroup transientInstance);
	public void update(String deviceId, String groupId);
	public RefTermGroup findById(RefTermGroupId id);
	public void deleteAll(String[] deviceIds);
}

