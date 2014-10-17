package com.sosgps.wzt.stat.dao;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAlarmShow;

/**
 * @Title:����ͳ�����ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2010-4-3 ����10:16:03
 */
public interface AlarmStatDao {

	public Page<Object[]> listAlarmsByToday(String entCode, Long userId,
			int pageNo, int pageSize);

	// sos��ѯ������¼
	// alarmType��������:1 ���ٱ���2 ���򱨾�3 ��������4��������6ƫ������
	public Page<Object[]> listAlarm(String entCode, Long userId, int pageNo,
			int pageSize, int alarmType, Date startTime, Date endTime,
			String searchValue, String deviceIds);

	public boolean removeAlarm(Long id);

	// sos��ѯ������¼(���б���)
	public Page<Object[]> listAllAlarm(String entCode, Long userId, int pageNo,
			int pageSize, Date startTime, Date endTime, String searchValue);
	
	public boolean removeAllAlarm(final String empCode);

}
