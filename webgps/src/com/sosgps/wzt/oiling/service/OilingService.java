package com.sosgps.wzt.oiling.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOiling;

public interface OilingService {

	public Page<Object[]> listOiling(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TOiling transientInstance);
	public void update(TOiling transientInstance);
	public void deleteAll(final String ids);
	
}
