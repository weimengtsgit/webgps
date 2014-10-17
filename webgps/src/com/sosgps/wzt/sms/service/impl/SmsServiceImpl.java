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
 * @Title:短信业务层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-24 上午10:32:53
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
			log.error("待发短信入库异常：" + Constants.ERROR_DAO_INSTANCE_NULL);
			return;
		}
		try {
			smsWaitDao.saveWaitSMS(entCode, phone, message);
		} catch (Exception e) {
			log.error("待发短信入库service异常", e);
		}
	}

	public void sendSms(String entCode, String[] phones, String message) {
		if (smsWaitDao == null) {
			log.error("待发短信入库异常：" + Constants.ERROR_DAO_INSTANCE_NULL);
			return;
		}
		try {
			for (String phone : phones) {
				smsWaitDao.saveWaitSMS(entCode, phone, message);
			}
		} catch (Exception e) {
			log.error("待发短信入库service异常", e);
		}
	}

	/**
	 * 获得短信特服号
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
			log.error("查询未读接收短信错误", e);
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
			log.error("查询所有接收短信错误", e);
			return null;
		}
	}

	public void readSms(String recvId) {
		try {
			ShandongSmsRecv persistentInstance = smsReceiveDao.findById(recvId);
			if (persistentInstance != null)
				smsReceiveDao.delete(persistentInstance);
		} catch (Exception e) {
			log.error("短信设置为已读错误", e);
		}
	}

	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue) {
		try {
			return smsWaitDao.listSendSmsLog(entCode, userId, pageNo, pageSize,
					startTime, endTime, searchValue);
		} catch (Exception e) {
			log.error("查询已发送短信错误", e);
			return null;
		}
	}

	// add by magiejue 2010-12-13查询所有接受短信未分页
	public List<Object> listReceiveSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue) {

		try {
			return smsReceiveDao.listReceiveSmsLog(entCode, userId, startTime,
					endTime, searchValue);
		} catch (Exception e) {
			log.error("查询所有接收短信错误", e);
			return null;
		}
	};

	// add by magiejue 2010-12-13 查询所有发送短信未分页
	public List<Object> listSendSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue) {
		try {
			return smsWaitDao.listSendSmsLog(entCode, userId, startTime,
					endTime, searchValue);
		} catch (Exception e) {
			log.error("查询所有发送短信错误", e);
			return null;
		}
	}

	// 查询企业短信统计信息 add by liuhongxiao 2011-12-08
	public List<SmsAccounts> findByEntCode(String entCode) {
		return smsAccountsDao.findByEntCode(entCode);
	}
	
	// add by liuhongxiao 2011-12-23
	public void save(SmsAccounts smsAccounts){
		smsAccountsDao.save(smsAccounts);
	}
}
