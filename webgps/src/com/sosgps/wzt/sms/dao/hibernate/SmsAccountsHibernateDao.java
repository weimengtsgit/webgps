package com.sosgps.wzt.sms.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;

import com.sosgps.wzt.orm.SmsAccounts;
import com.sosgps.wzt.orm.SmsRechargeAccountsDao;
import com.sosgps.wzt.sms.dao.SmsAccountsDao;

/**
 * @Title:短信统计dao层接口具体实现类
 * @Description:
 * @Company:
 * @author:
 * @version 1.0
 * @date: 2011-12-08
 */
public class SmsAccountsHibernateDao extends SmsRechargeAccountsDao implements
		SmsAccountsDao {
	private Logger log = Logger.getLogger(SmsAccountsHibernateDao.class);

	public List<SmsAccounts> findByEntCode(String entCode) {
		return this.getHibernateTemplate().find(
				"select s from SmsAccounts s where s.entCode = ?",
				new Object[] { entCode });
	}
}
