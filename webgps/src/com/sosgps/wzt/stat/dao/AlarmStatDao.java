package com.sosgps.wzt.stat.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;

/**
 * @Title:报警统计数据层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 上午10:16:03
 */
public interface AlarmStatDao {

	public Page<Object[]> listAlarmsByToday(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos查询报警记录
	// alarmType报警类型:1 超速报警2 区域报警3 主动报警4紧急报警6偏航报警
	public Page<Object[]> listAlarm(String entCode, Long userId, int pageNo,
			int pageSize, int alarmType, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	public boolean removeAlarm(Long id);

	// sos查询报警记录(所有报警)
	public Page<Object[]> listAllAlarm(String entCode, Long userId, int pageNo,
			int pageSize, Date startTime, Date endTime, String searchValue);
	
	public boolean removeAllAlarm(final String empCode);

}
