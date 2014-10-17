package com.sosgps.wzt.stat.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:里程统计业务层接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:41:59
 */
public interface DistanceStatService {

	// sos日里程统计
	Page<Object[]> listDistanceStatByDay(String entCode, Long userId,
			int pageNo, int pageSize, Date theDay, String searchValue, String deviceIdss);

	// sos月里程统计
	Page<Object[]> listDistanceStatByMonth(String entCode, Long userId,
			int pageNo, int pageSize, Date theMonth, String searchValue, String deviceIdss);

	// sos年里程统计
	Page<Object[]> listDistanceStatByYear(String entCode, Long userId,
			int pageNo, int pageSize, Date theYear, String searchValue, String deviceIdss);

	// sos自定义里程统计
	Page<Object[]> listDistanceStatByCustom(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIdss);
	
	Page<Object[]> listTotalDistanceStat(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) ;
	//按时间段查询里程
	public Page<Object[]> listTimeDistanceStatByCustom(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds, String searchValue);
	
	//考勤统计
	Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
