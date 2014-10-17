package com.sosgps.wzt.toll.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface TollDao {
	public Page<Object[]> listToll(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
