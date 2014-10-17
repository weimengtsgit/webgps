package com.sosgps.wzt.carTypeInfo.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.CarTypeInfo;
import com.sosgps.wzt.orm.TOiling;

public interface CarTypeInfoService {

	public Page<Object[]> listCarTypeInfo(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(CarTypeInfo transientInstance);
	public void update(CarTypeInfo transientInstance);
	public void deleteAll(final String ids);
	
}
