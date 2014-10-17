package com.sosgps.wzt.vehiclenorm.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface VehicleNormDao {
	public Page<Object[]> listvehicleNormDaoImpl(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
