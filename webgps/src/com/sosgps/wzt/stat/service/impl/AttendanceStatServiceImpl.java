package com.sosgps.wzt.stat.service.impl;

import org.apache.log4j.Logger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.stat.dao.AttendanceStatDao;
import com.sosgps.wzt.stat.service.AttendanceStatService;

/**
 * @Title:签到签出统计业务层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:42:23
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
			logger.error("查询签到签出统计错误", e);
			return null;
		}
	}
	
}
