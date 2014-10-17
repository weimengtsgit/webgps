package com.sosgps.wzt.stat.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

/**
 * @Title:���ͳ��dao�ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2010-4-24 ����12:58:37
 */
public interface DistanceStatDao {
	// sos�����ͳ��
	Page<Object[]> listDistanceStatByMonth(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds);

	// sos�����ͳ��
	Page<Object[]> listDistanceStatByYear(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds);

	// sos�Զ������ͳ��
	Page<Object[]> listDistanceStatByCustom(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue, String deviceIds);
	
	public Page<Object[]> listTotalDistanceStat(String entCode, Long userId,
			int pageNo, int pageSize, String searchValue, String deviceIds);
	
	//��ʱ��β�ѯ���
	public Page<Object[]> listTimeDistanceStatByCustom(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds, String searchValue);
	
	//����ͳ��
	public Page<Object[]> listAttendanceRecord(int pageNo, int pageSize, String startDate, 
			String endDate,String deviceIds);
	
}
