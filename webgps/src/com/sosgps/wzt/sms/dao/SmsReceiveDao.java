package com.sosgps.wzt.sms.dao;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsRecv;

/**
 * @Title:���Ž���dao��ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-25 ����10:58:07
 */
public interface SmsReceiveDao {
	// sos��ѯδ������
	public Page<Object[]> listNotReadReceiveSms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// sos��ѯ���н��ն���
	public Page<Object[]> listReceiveSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);
	
	public ShandongSmsRecv findById( java.lang.String id);
	// sos��������Ϊ�Ѷ�
	public void delete(ShandongSmsRecv persistentInstance);
	//add by magiejue��ѯ���н��ն���Ϊδҳ
	public List<Object> listReceiveSmsLog(String entCode, Long userId,Date startTime, Date endTime,String searchValue);
}
