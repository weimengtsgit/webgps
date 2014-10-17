package com.sosgps.wzt.oiling.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface OilingDao {
	public Page<Object[]> listOiling(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
