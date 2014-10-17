package com.sosgps.wzt.stat.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;

/**
 * @Title:报警统计业务层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 上午10:12:08
 */
public interface AlarmStatService {
	// sos查询当日报警提示信息列表
	public Page<Object[]> listAlarmsByToday(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos解除报警
	public boolean removeAlarm(String showId);

	// sos查询超速报警
	public Page<Object[]> listSpeedAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos查询区域报警
	public Page<Object[]> listAreaAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos查询主动报警
	public Page<Object[]> listHoldAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos查询偏航报警
	public Page<Object[]> listLineAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos查询所有报警
	public Page<Object[]> listAllAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);
	public boolean removeAllAlarm(final String empCode) ;
}
