package com.sosgps.wzt.insurance.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TInsurance;

public interface InsuranceService {

	public Page<Object[]> listInsurance(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TInsurance transientInstance);
	public void update(TInsurance transientInstance);
	public void deleteAll(final String ids);
	
}
