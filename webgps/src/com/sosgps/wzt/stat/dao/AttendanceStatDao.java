package com.sosgps.wzt.stat.dao;

import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:签到签出统计dao接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:58:37
 */
public interface AttendanceStatDao {
	//考勤统计
	public Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
