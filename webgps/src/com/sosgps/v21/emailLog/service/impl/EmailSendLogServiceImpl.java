package com.sosgps.v21.emailLog.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sosgps.v21.dao.EmailSendLogDAO;
import com.sosgps.v21.emailLog.service.EmailSendLogService;
import com.sosgps.v21.model.EmailSendLog;
import com.sosgps.wzt.manage.util.Page;

public class EmailSendLogServiceImpl implements EmailSendLogService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSendLogServiceImpl.class);
    
    private EmailSendLogDAO emailLogDao;


	public EmailSendLogDAO getEmailLogDao() {
		return emailLogDao;
	}

	public void setEmailLogDao(EmailSendLogDAO emailLogDao) {
		this.emailLogDao = emailLogDao;
	}

	public Page<EmailSendLog> listEmailLogs(Long startDateL, Long endDateL,
			String entCode, String entName, String contactName, int pageNo,
			int pageSize) {
		return emailLogDao.listEmailLogs(startDateL, endDateL, entCode,
				entName, contactName, pageNo, pageSize);
	}

	public Page<EmailSendLog> listEmailLogs(Long startDateL, Long endDateL,
			String entCode, String entName, String contactName) {
		return emailLogDao.listEmailLogs(startDateL, endDateL, entCode,
				entName, contactName);
	}

	public void deleteEmailLogs(Long[] ids) {
        emailLogDao.deleteEmailLogs(ids);
    }

}
