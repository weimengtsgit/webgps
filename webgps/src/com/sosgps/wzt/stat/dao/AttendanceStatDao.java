package com.sosgps.wzt.stat.dao;

import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:ǩ��ǩ��ͳ��dao�ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����12:58:37
 */
public interface AttendanceStatDao {
	//����ͳ��
	public Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
