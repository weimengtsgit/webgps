package com.sosgps.v21.dao;

import com.sosgps.v21.model.EmailSendLog;
import com.sosgps.wzt.manage.util.Page;

/**
 * 
 * 
 * @see com.sosgps.V21.model.EmailSendLog
 * @author qiang zhou
 */

public interface EmailSendLogDAO {

	public Page<EmailSendLog> listEmailLogs(Long startDate, Long endDate,
			String entCode, String entName, String contactName, int pageNo,
			int pageSize);

	public Page<EmailSendLog> listEmailLogs(Long startDate, Long endDate,
			String entCode, String entName, String contactName);

	public void deleteEmailLogs(Long[] ids);
}
