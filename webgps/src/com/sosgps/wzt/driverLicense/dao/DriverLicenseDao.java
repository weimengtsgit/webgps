package com.sosgps.wzt.driverLicense.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface DriverLicenseDao {
	public Page<Object[]> listDriverLicense(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
