package com.sosgps.wzt.manage.terminal.dao;

import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.RefTermGroupId;
/**
 * @Title:终端属于终端组数据层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-30 下午02:51:46
 */
public interface RefTermGroupDao {
	public void save(RefTermGroup transientInstance);
	public void update(String deviceId, String groupId);
	public RefTermGroup findById(RefTermGroupId id);
	public void deleteAll(String[] deviceIds);
}

