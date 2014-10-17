package com.sosgps.wzt.stat.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;
import com.sosgps.wzt.stat.dao.AlarmStatDao;
import com.sosgps.wzt.stat.service.AlarmStatService;

/**
 * @Title:����ͳ��ҵ���ӿھ���ʵ����
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 ����10:12:22
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
			logger.error("��ѯ���ձ�����ʾ��Ϣ����", e);
			return null;
		}
	}

	public boolean removeAlarm(String showId) {
		try {
			Long id = Long.parseLong(showId);
			return alarmStatDao.removeAlarm(id);
		} catch (Exception e) {
			logger.error("�����������", e);
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
			logger.error("��ѯ���򱨾�����", e);
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
			logger.error("��ѯ������������", e);
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
			logger.error("��ѯƫ����������", e);
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
			logger.error("��ѯ���ٱ�������", e);
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
			logger.error("��ѯ���б�������", e);
			return null;
		}
	}
	
	public boolean removeAllAlarm(final String empCode) {
		try {
			return alarmStatDao.removeAllAlarm(empCode);
		} catch (Exception e) {
			logger.error("�����������", e);
			return false;
		}
	}
	
}
