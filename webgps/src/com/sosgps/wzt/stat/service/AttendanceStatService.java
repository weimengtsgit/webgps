package com.sosgps.wzt.stat.service;

import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:ǩ��ǩ��ͳ��ҵ���ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����12:41:59
 */
public interface AttendanceStatService {

	//����ͳ��
	Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
