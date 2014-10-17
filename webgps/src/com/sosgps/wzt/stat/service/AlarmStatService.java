package com.sosgps.wzt.stat.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;

/**
 * @Title:����ͳ��ҵ���ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 ����10:12:08
 */
public interface AlarmStatService {
	// sos��ѯ���ձ�����ʾ��Ϣ�б�
	public Page<Object[]> listAlarmsByToday(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos�������
	public boolean removeAlarm(String showId);

	// sos��ѯ���ٱ���
	public Page<Object[]> listSpeedAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos��ѯ���򱨾�
	public Page<Object[]> listAreaAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos��ѯ��������
	public Page<Object[]> listHoldAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos��ѯƫ������
	public Page<Object[]> listLineAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	// sos��ѯ���б���
	public Page<Object[]> listAllAlarms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);
	public boolean removeAllAlarm(final String empCode) ;
}
