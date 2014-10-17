package com.sosgps.wzt.sms.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.ShandongSmsRecv;
import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.sms.dao.SmsAccountsDao;
import com.sosgps.wzt.sms.dao.SmsReceiveDao;
import com.sosgps.wzt.sms.dao.SmsWaitDao;
import com.sosgps.wzt.sms.service.SmsService;

/**
 * @Title:����ҵ���ӿھ���ʵ����
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-24 ����10:32:53
 */
public class SmsServiceImpl implements SmsService {
	private SmsWaitDao smsWaitDao;
	private String srcNumber;
	private SmsReceiveDao smsReceiveDao;
	private SmsAccountsDao smsAccountsDao;
	private Logger log = Logger.getLogger(SmsServiceImpl.class);

	public SmsWaitDao getSmsWaitDao() {
		return smsWaitDao;
	}

	public void setSmsWaitDao(SmsWaitDao smsWaitDao) {
		this.smsWaitDao = smsWaitDao;
	}

	public void sendSms(String entCode, String phone, String message) {
		if (smsWaitDao == null) {
			log.error("������������쳣��" + Constants.ERROR_DAO_INSTANCE_NULL);
			return;
		}
		try {
			smsWaitDao.saveWaitSMS(entCode, phone, message);
		} catch (Exception e) {
			log.error("�����������service�쳣", e);
		}
	}

	public void sendSms(String entCode, String[] phones, String message) {
		if (smsWaitDao == null) {
			log.error("������������쳣��" + Constants.ERROR_DAO_INSTANCE_NULL);
			return;
		}
		try {
			for (String phone : phones) {
				smsWaitDao.saveWaitSMS(entCode, phone, message);
			}
		} catch (Exception e) {
			log.error("�����������service�쳣", e);
		}
	}

	/**
	 * ��ö����ط���
	 * 
	 * @return
	 */
	public String getSmsServiceId() {
		return this.srcNumber;
	}

	public String getSrcNumber() {
		return srcNumber;
	}

	public void setSrcNumber(String srcNumber) {
		this.srcNumber = srcNumber;
	}

	public SmsReceiveDao getSmsReceiveDao() {
		return smsReceiveDao;
	}

	public void setSmsReceiveDao(SmsReceiveDao smsReceiveDao) {
		this.smsReceiveDao = smsReceiveDao;
	}

	public SmsAccountsDao getSmsAccountsDao() {
		return smsAccountsDao;
	}

	public void setSmsAccountsDao(SmsAccountsDao smsAccountsDao) {
		this.smsAccountsDao = smsAccountsDao;
	}

	public Page<Object[]> listNotReadReceiveSms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		try {
			return smsReceiveDao.listNotReadReceiveSms(entCode, userId, pageNo,
					pageSize, startTime, endTime, searchValue);
		} catch (Exception e) {
			log.error("��ѯδ�����ն��Ŵ���", e);
			return null;
		}
	}

	public Page<Object[]> listReceiveSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		try {
			return smsReceiveDao.listReceiveSmsLog(entCode, userId, pageNo,
					pageSize, startTime, endTime, searchValue);
		} catch (Exception e) {
			log.error("��ѯ���н��ն��Ŵ���", e);
			return null;
		}
	}

	public void readSms(String recvId) {
		try {
			ShandongSmsRecv persistentInstance = smsReceiveDao.findById(recvId);
			if (persistentInstance != null)
				smsReceiveDao.delete(persistentInstance);
		} catch (Exception e) {
			log.error("��������Ϊ�Ѷ�����", e);
		}
	}

	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		try {
			return smsWaitDao.listSendSmsLog(entCode, userId, pageNo, pageSize,
					startTime, endTime, searchValue);
		} catch (Exception e) {
			log.error("��ѯ�ѷ��Ͷ��Ŵ���", e);
			return null;
		}
	}

	// add by magiejue 2010-12-13��ѯ���н��ܶ���δ��ҳ
	public List<Object> listReceiveSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue) {

		try {
			return smsReceiveDao.listReceiveSmsLog(entCode, userId, startTime,
					endTime, searchValue);
		} catch (Exception e) {
			log.error("��ѯ���н��ն��Ŵ���", e);
			return null;
		}
	};

	// add by magiejue 2010-12-13 ��ѯ���з��Ͷ���δ��ҳ
	public List<Object> listSendSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue) {
		try {
			return smsWaitDao.listSendSmsLog(entCode, userId, startTime,
					endTime, searchValue);
		} catch (Exception e) {
			log.error("��ѯ���з��Ͷ��Ŵ���", e);
			return null;
		}
	}

	// ��ѯ��ҵ����ͳ����Ϣ add by liuhongxiao 2011-12-08
	public List<SmsAccounts> findByEntCode(String entCode) {
		return smsAccountsDao.findByEntCode(entCode);
	}
	
	// add by liuhongxiao 2011-12-23
	public void save(SmsAccounts smsAccounts){
		smsAccountsDao.save(smsAccounts);
	}
}
