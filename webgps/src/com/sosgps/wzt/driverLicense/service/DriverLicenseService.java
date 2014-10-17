package com.sosgps.wzt.driverLicense.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDriverLicense;

public interface DriverLicenseService {
	public Page<Object[]> listDriverLicense(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TDriverLicense transientInstance);
	public void update(TDriverLicense transientInstance);
	public void deleteAll(final String ids);
	
}
