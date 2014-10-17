package com.sosgps.wzt.struction.service;

import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-18 ����07:52:39
 */
public interface StructionService {
	// sos������ٱ���
	public void stopSpeedAlarm(String deviceId);

	// sos����پ�
	public void stopHoldAlarm(String deviceId);

	// sos���Ͷϵ�
	public void turnOffOilPower(String deviceId);

	// sos�ָ����Ͷϵ�
	public void turnOnOilPower(String deviceId);

	// add by 2012-12-17 zss ������򱨾�
	public void stopAreaAlarm(String deviceId);

	// ָ����Ϣͳ��
	Page<Object[]> listStructionsRecord(int pageNo, int pageSize,
			String startDate, String endDate, String deviceIds, String type);
}
