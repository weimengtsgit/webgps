package com.sosgps.wzt.sms.service;

import java.util.Date;
import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.orm.TEnt;

/**
 * @Title:短信业务层接口
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2009-3-24 下午03:09:48
 */
public interface SmsService {
	public void sendSms(String entCode, String phone, String message);

	public void sendSms(String entCode, String[] phone, String message);

	/**
	 * 获得短信特服号
	 * 
	 * @return
	 */
	public String getSmsServiceId();

	// sos查询未读短信
	public Page<Object[]> listNotReadReceiveSms(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// sos查询所有接收短信
	public Page<Object[]> listReceiveSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// sos短信设置为已读
	public void readSms(String recvId);

	// sos查询已发送短信
	public Page<Object[]> listSendSmsLog(String entCode, Long userId,
			int pageNo, int pageSize, Date startTime, Date endTime,
			String searchValue);

	// add by magiejue 2010-12-13查询所有接受短信未分页
	public List<Object> listReceiveSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue);

	// add by magiejue 2010-12-13 查询所有发送短信未分页
	public List<Object> listSendSmsLog(String entCode, Long userId,
			Date startTime, Date endTime, String searchValue);

	// 查询企业短信统计信息 add by liuhongxiao 2011-12-08
	public List<SmsAccounts> findByEntCode(String entCode);
	
	// add by liuhongxiao 2011-12-23
	public void save(SmsAccounts smsAccounts);
}
