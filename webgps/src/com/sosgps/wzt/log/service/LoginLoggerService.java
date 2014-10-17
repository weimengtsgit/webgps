package com.sosgps.wzt.log.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLoginLog;

public interface LoginLoggerService {
	public void save(TLoginLog transientInstance)throws Exception;
	public boolean deleteAll(Long[] ids) throws Exception;
	public Page<TLoginLog> queryLoginLog(String entCode,
			String deviceIds, String startTime, String endTime, String pageNo,
			String pageSize, String paramName, String paramValue, String vague,
			boolean autoCount, String type);

	// sos≤È—Øµ«¬º»’÷æ
	public Page<TLoginLog> listLoginLog(String entCode, int pageNo,
			int pageSize, Date startTime, Date endTime, String searchValue);
}
