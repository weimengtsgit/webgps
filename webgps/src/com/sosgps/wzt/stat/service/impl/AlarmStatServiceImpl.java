package com.sosgps.wzt.stat.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;
import com.sosgps.wzt.stat.dao.AlarmStatDao;
import com.sosgps.wzt.stat.service.AlarmStatService;

/**
 * @Title:报警统计业务层接口具体实现类
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 上午10:12:22
 */
public class AlarmStatServiceImpl implements AlarmStatService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AlarmStatServiceImpl.class);

	private AlarmStatDao alarmStatDao;

	public AlarmStatDao getAlarmStatDao() {
		return alarmStatDao;
	}

	public void setAlarmStatDao(AlarmStatDao alarmStatDao) {
		this.alarmStatDao = alarmStatDao;
	}

	public Page<Object[]> listAlarmsByToday(String entCode, Long userId,
			int pageNo, int pageSize) {
		try {
			return alarmStatDao.listAlarmsByToday(entCode, userId, pageNo,
					pageSize);
		} catch (Exception e) {
			logger.error("查询当日报警提示信息错误", e);
			return null;
		}
	}

	public boolean removeAlarm(String showId) {
		try {
			Long id = Long.parseLong(showId);
			return alarmStatDao.removeAlarm(id);
		} catch (Exception e) {
			logger.error("解除报警错误", e);
			return false;
		}
	}

	public Page<Object[]> listAreaAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds) {
		try {
			return alarmStatDao.listAlarm(entCode, userId, pageNo, pageSize, 2,
					startTime, endTime, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("查询区域报警错误", e);
			return null;
		}
	}

	public Page<Object[]> listHoldAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds) {
		try {
			return alarmStatDao.listAlarm(entCode, userId, pageNo, pageSize, 3,
					startTime, endTime, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("查询主动报警错误", e);
			return null;
		}
	}

	public Page<Object[]> listLineAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds) {
		try {
			return alarmStatDao.listAlarm(entCode, userId, pageNo, pageSize, 6,
					startTime, endTime, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("查询偏航报警错误", e);
			return null;
		}
	}

	public Page<Object[]> listSpeedAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds) {
		try {
			return alarmStatDao.listAlarm(entCode, userId, pageNo, pageSize, 1,
					startTime, endTime, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("查询超速报警错误", e);
			return null;
		}
	}

	public Page<Object[]> listAllAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		try {
			return alarmStatDao.listAllAlarm(entCode, userId, pageNo, pageSize,
					startTime, endTime, searchValue);
		} catch (Exception e) {
			logger.error("查询所有报警错误", e);
			return null;
		}
	}
	
	public boolean removeAllAlarm(final String empCode) {
		try {
			return alarmStatDao.removeAllAlarm(empCode);
		} catch (Exception e) {
			logger.error("解除报警错误", e);
			return false;
		}
	}
	
}
