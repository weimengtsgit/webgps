package com.sosgps.wzt.sms.service;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.orm.TEnt;

/**
 * @Title:����ҵ���ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-24 ����03:09:48
 */
public interface SmsService {
	public void sendSms(String entCode, String phone, String message);

	public void sendSms(String entCode, String[] phone, String message);

	/**
	 * ��ö����ط���
	 * 
	 * @return
	 */
	public String getSmsServiceId();

	// sos��ѯδ������
	public Page<Object[]> listNotReadReceiveSms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// sos��ѯ���н��ն���
	public Page<Object[]> listReceiveSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// sos��������Ϊ�Ѷ�
	public void readSms(String recvId);

	// sos��ѯ�ѷ��Ͷ���
	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// add by magiejue 2010-12-13��ѯ���н��ܶ���δ��ҳ
	public List<Object> listReceiveSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue);

	// add by magiejue 2010-12-13 ��ѯ���з��Ͷ���δ��ҳ
	public List<Object> listSendSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue);

	// ��ѯ��ҵ����ͳ����Ϣ add by liuhongxiao 2011-12-08
	public List<SmsAccounts> findByEntCode(String entCode);
	
	// add by liuhongxiao 2011-12-23
	public void save(SmsAccounts smsAccounts);
}
