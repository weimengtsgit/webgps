package com.sosgps.wzt.carTypeInfo.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface CarTypeInfoDao {
	public Page<Object[]> listCarTypeInfo(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
