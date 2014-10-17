package com.sosgps.wzt.log.service.impl;

import org.apache.log4j.Logger;

import java.util.Date;

import com.sosgps.wzt.log.dao.LoginLogDao;
import com.sosgps.wzt.log.service.LoginLoggerService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLoginLog;

public class LoginLoggerServiceImpl implements LoginLoggerService{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(LoginLoggerServiceImpl.class);

	private LoginLogDao loginLogDao;
	public void save(TLoginLog transientInstance)throws Exception{
		try{
			this.loginLogDao.save(transientInstance);
			}catch(RuntimeException ex){
				throw ex;
			}
	}
	public Page<TLoginLog> queryLoginLog(String entCode, String deviceIds,
			String startTime, String endTime, String pageNo, String pageSize,
			String paramName, String paramValue, String vague,
			boolean autoCount, String type) {

		return loginLogDao
				.queryLoginLog(entCode, deviceIds, startTime, endTime, pageNo,
						pageSize, paramName, paramValue, vague, autoCount, type);

	}
	public boolean deleteAll(Long[] ids) throws Exception{
		boolean ret = false;
		ret = this.loginLogDao.deleteAll(ids);
		return ret;
	}
	public LoginLogDao getLoginLogDao() {
		return loginLogDao;
	}
	public void setLoginLogDao(LoginLogDao loginLogDao) {
		this.loginLogDao = loginLogDao;
	}

	public Page<TLoginLog> listLoginLog(String entCode, int pageNo,
			int pageSize, Date startTime, Date endTime, String searchValue) {
		try {
			return loginLogDao.listLoginLog(entCode, pageNo, pageSize,
					startTime, endTime, searchValue);
		} catch (Exception e) {
			logger.error("≤È—Øµ«¬º»’÷æ¥ÌŒÛ", e);
		}
		return null;
	}
}
