package com.sosgps.wzt.stat.service;

import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:签到签出统计业务层接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 下午12:41:59
 */
public interface AttendanceStatService {

	//考勤统计
	Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
