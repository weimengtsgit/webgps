package com.sosgps.wzt.insurance.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface InsuranceDao {
	public Page<Object[]> listInsurance(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
