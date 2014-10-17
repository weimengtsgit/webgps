package com.sosgps.wzt.stat.service.impl;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDistanceDay;
import com.sosgps.wzt.stat.dao.DistanceStatDao;
import com.sosgps.wzt.stat.service.DistanceStatService;

/**
 * @Title:里程统计业务层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:42:23
 */
public class DistanceStatServiceImpl implements DistanceStatService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(DistanceStatServiceImpl.class);
	private DistanceStatDao distanceStatDao;

	public DistanceStatDao getDistanceStatDao() {
		return distanceStatDao;
	}

	public void setDistanceStatDao(DistanceStatDao distanceStatDao) {
		this.distanceStatDao = distanceStatDao;
	}

	public Page<Object[]> listDistanceStatByCustom(String entCode,
			Long userId, int pageNo, int pageSize, Date startDate,
			Date endDate, String searchValue, String deviceIdss) {
		try {
			return distanceStatDao.listDistanceStatByCustom(entCode, userId,
					pageNo, pageSize, startDate, endDate, searchValue, deviceIdss);
		} catch (Exception e) {
			logger.error("自定义里程统计错误", e);
			return null;
		}
	}

	public Page<Object[]> listDistanceStatByDay(String entCode,
			Long userId, int pageNo, int pageSize, Date theDay,
			String searchValue, String deviceIdss) {
		try {
			Date startDate = theDay;
			// 次日
			Calendar c = Calendar.getInstance();
			c.setTime(theDay);
			c.add(Calendar.DAY_OF_MONTH, 1);
			Date endDate = c.getTime();
			return distanceStatDao.listDistanceStatByCustom(entCode, userId,
					pageNo, pageSize, startDate, endDate, searchValue, deviceIdss);
		} catch (Exception e) {
			logger.error("日里程统计错误", e);
			return null;
		}
	}

	public Page<Object[]> listDistanceStatByMonth(String entCode,
			Long userId, int pageNo, int pageSize, Date theMonth,
			String searchValue, String deviceIdss) {
		try {
			Date startDate = theMonth;
			// 次月
			Calendar c = Calendar.getInstance();
			c.setTime(theMonth);
			c.add(Calendar.MONTH, 1);
			Date endDate = c.getTime();
			return distanceStatDao.listDistanceStatByMonth(entCode, userId,
					pageNo, pageSize, startDate, endDate, searchValue, deviceIdss);

		} catch (Exception e) {
			logger.error("月里程统计错误", e);
			return null;
		}
	}

	public Page<Object[]> listDistanceStatByYear(String entCode,
			Long userId, int pageNo, int pageSize, Date theYear,
			String searchValue, String deviceIdss) {
		try {
			Date startDate = theYear;
			// 次年
			Calendar c = Calendar.getInstance();
			c.setTime(theYear);
			c.add(Calendar.YEAR, 1);
			Date endDate = c.getTime();
			return distanceStatDao.listDistanceStatByYear(entCode, userId,
					pageNo, pageSize, startDate, endDate, searchValue, deviceIdss);
		} catch (Exception e) {
			logger.error("年里程统计错误", e);
			return null;
		}
	}

	public Page<Object[]> listTotalDistanceStat(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) {
		try {
			return distanceStatDao.listTotalDistanceStat(entCode, userId,
					pageNo, pageSize, searchValue, deviceIds);
		} catch (Exception e) {
			logger.error("总里程统计错误", e);
			return null;
		}
	}
	//按时间段查询里程
	public Page<Object[]> listTimeDistanceStatByCustom(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds, String searchValue) {
		try {
			return distanceStatDao.listTimeDistanceStatByCustom(pageNo, pageSize, startDate, endDate, deviceIds, searchValue);
		} catch (Exception e) {
			logger.error("按时间段查询里程统计错误", e);
			return null;
		}
	}

	public Page<Object[]> listAttendanceRecord(int pageNo, int pageSize,
			String startDate, String endDate, String deviceIds) {
		try {
			return distanceStatDao.listAttendanceRecord(pageNo, pageSize, startDate, endDate, deviceIds);
		} catch (Exception e) {
			logger.error("按时间段查询里程统计错误", e);
			return null;
		}
	}
	
}
