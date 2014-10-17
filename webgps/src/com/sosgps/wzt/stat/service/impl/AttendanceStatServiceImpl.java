package com.sosgps.wzt.stat.service.impl;

import org.apache.log4j.Logger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.stat.dao.AttendanceStatDao;
import com.sosgps.wzt.stat.service.AttendanceStatService;

/**
 * @Title:ǩ��ǩ��ͳ��ҵ���ӿھ���ʵ����
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����12:42:23
 */
public class AttendanceStatServiceImpl implements AttendanceStatService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(AttendanceStatServiceImpl.class);
	private AttendanceStatDao attendanceStatDao;

	public AttendanceStatDao getAttendanceStatDao() {
		return attendanceStatDao;
	}

	public void setAttendanceStatDao(AttendanceStatDao attendanceStatDao) {
		this.attendanceStatDao = attendanceStatDao;
	}

	public Page<Object[]> listAttendanceRecord(int pageNo, int pageSize,
			String startDate, String endDate, String deviceIds) {
		try {
			return attendanceStatDao.listAttendanceRecord(pageNo, pageSize, startDate, endDate, deviceIds);
		} catch (Exception e) {
			logger.error("��ѯǩ��ǩ��ͳ�ƴ���", e);
			return null;
		}
	}
	
}
