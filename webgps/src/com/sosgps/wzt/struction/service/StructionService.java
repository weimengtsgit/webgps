package com.sosgps.wzt.struction.service;

import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-18 下午07:52:39
 */
public interface StructionService {
	// sos解除超速报警
	public void stopSpeedAlarm(String deviceId);

	// sos解除劫警
	public void stopHoldAlarm(String deviceId);

	// sos断油断电
	public void turnOffOilPower(String deviceId);

	// sos恢复断油断电
	public void turnOnOilPower(String deviceId);

	// add by 2012-12-17 zss 解除区域报警
	public void stopAreaAlarm(String deviceId);

	// 指令信息统计
	Page<Object[]> listStructionsRecord(int pageNo, int pageSize,
			String startDate, String endDate, String deviceIds, String type);
}
