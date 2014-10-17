package com.sosgps.wzt.stat.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:���ͳ��ҵ���ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����12:41:59
 */
public interface DistanceStatService {

	// sos�����ͳ��
	Page<Object[]> listDistanceStatByDay(String entCode, Long userId,
			int pageNo, int pageSize, Date theDay, String searchValue, String deviceIdss);

	// sos�����ͳ��
	Page<Object[]> listDistanceStatByMonth(String entCode, Long userId,
			int pageNo, int pageSize, Date theMonth, String searchValue, String deviceIdss);

	// sos�����ͳ��
	Page<Object[]> listDistanceStatByYear(String entCode, Long userId,
			int pageNo, int pageSize, Date theYear, String searchValue, String deviceIdss);

	// sos�Զ������ͳ��
	Page<Object[]> listDistanceStatByCustom(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIdss);
	
	Page<Object[]> listTotalDistanceStat(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds) ;
	//��ʱ��β�ѯ���
	public Page<Object[]> listTimeDistanceStatByCustom(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds, String searchValue);
	
	//����ͳ��
	Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
