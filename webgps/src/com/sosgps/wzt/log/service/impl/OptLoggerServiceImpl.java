package com.sosgps.wzt.log.service.impl;

import org.apache.log4j.Logger;

import java.util.Date;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.service.OptLoggerService;
import com.sosgps.wzt.orm.TAreaLocrecord;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.PageQuery;
import com.sosgps.wzt.log.dao.OptLogDao;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.util.Page;

/**
 * <p>
 * Title: LoginAction
 * </p>
 * <p>
 * Description: ������־����ʵ����
 * </p>
 * @version 1.0
 */
public class OptLoggerServiceImpl implements OptLoggerService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(OptLoggerServiceImpl.class);

	private OptLogDao optLogDao;

	// ���������־
	public void save(TOptLog transientInstance) throws Exception {
		try { 
			this.optLogDao.save(transientInstance);
		} catch (RuntimeException ex) {
			// SysLogger.sysLogger.error("E070:
			// ������־-���������־ʧ��,����ԭ��"+ex.getMessage(),ex);
			throw ex;
		}
	}

	public Page<TOptLog> queryOptLog(String entCode, String deviceIds,
			String startTime, String endTime, String pageNo, String pageSize,
			String paramName, String paramValue, String vague,
			boolean autoCount, String type) {

		return optLogDao
				.queryOptLog(entCode, deviceIds, startTime, endTime, pageNo,
						pageSize, paramName, paramValue, vague, autoCount, type);

	}

	// ɾ��������־
	public boolean deleteAll(Long[] ids) throws Exception {
		boolean ret = false;
		ret = this.optLogDao.deleteAll(ids);
		return ret;
	}

	public OptLogDao getOptLogDao() {
		return optLogDao;
	}

	public void setOptLogDao(OptLogDao optLogDao) {
		this.optLogDao = optLogDao;
	}

	public Page<TOptLog> listOptLog(String entCode, int pageNo, int pageSize,
			Date startTime, Date endTime, String searchValue) {
		try {
			return optLogDao.listOptLog(entCode, pageNo, pageSize,
					startTime, endTime, searchValue);
			} catch (Exception e) {
				logger.error("��ѯ��¼��־����", e);
			}
		return null;
	}
}
