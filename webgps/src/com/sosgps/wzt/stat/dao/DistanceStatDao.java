package com.sosgps.wzt.stat.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:里程统计dao接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:58:37
 */
public interface DistanceStatDao {
	// sos月里程统计
	Page<Object[]> listDistanceStatByMonth(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds);

	// sos年里程统计
	Page<Object[]> listDistanceStatByYear(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds);

	// sos自定义里程统计
	Page<Object[]> listDistanceStatByCustom(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds);
	
	public Page<Object[]> listTotalDistanceStat(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds);
	
	//按时间段查询里程
	public Page<Object[]> listTimeDistanceStatByCustom(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds, String searchValue);
	
	//考勤统计
	public Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
