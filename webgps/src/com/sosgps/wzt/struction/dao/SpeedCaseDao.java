package com.sosgps.wzt.struction.dao;

import com.sosgps.wzt.orm.TSpeedCase;

/**
 * @Title:�������� ���ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2009-2-24 ����11:18:59
 */
public interface SpeedCaseDao {
	public void attachDirty(TSpeedCase instance);
	public void save(TSpeedCase transientInstance);
	public TSpeedCase findByDeviceId(String deviceId);
}
