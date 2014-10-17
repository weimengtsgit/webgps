package com.sosgps.wzt.sms.dao;

import java.util.List;

import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.orm.TEnt;

/**
 * @Title:����ͳ��dao��ӿ�
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2011-12-08
 */
public interface SmsAccountsDao {
	public List<SmsAccounts> findByEntCode(String entCode);
	public void save(SmsAccounts smsAccounts);
}
