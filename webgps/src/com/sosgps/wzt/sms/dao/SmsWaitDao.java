package com.sosgps.wzt.sms.dao;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsWait;

/**
 * @Title:�����������dao��ӿ�
 * @Description:
 * @Company: 
 * @author: 
 * @version 1.0
 * @date: 2009-3-24 ����03:03:18
 */
public interface SmsWaitDao {
	
	public void saveWaitSMS(String entCode, String phone, String message);

	// sos��ѯ�ѷ��Ͷ���
	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);
	//add by magiejue 2010-12-13 ��ѯ���з��Ͷ���δ��ҳ
	public List<Object> listSendSmsLog(String entCode, Long userId,Date startTime, Date endTime,String searchValue);
}
