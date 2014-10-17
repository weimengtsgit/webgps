package com.sosgps.wzt.vehiclenorm.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDispatchMoney;
import com.sosgps.wzt.orm.TVehicleNorm;

public interface VehicleNormService {
	public Page<Object[]> listVehicleNorm(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TVehicleNorm transientInstance);
	public void update(TVehicleNorm transientInstance);
	public void deleteAll(final String ids);
	
}
