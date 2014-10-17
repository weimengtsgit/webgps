package com.sosgps.wzt.driverLicense.service.impl;

import java.util.Date;
import com.sosgps.wzt.driverLicense.dao.hibernate.DriverLicenseDaoImpl;
import com.sosgps.wzt.driverLicense.service.DriverLicenseService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDriverLicense;

public class DriverLicenseServiceImpl implements DriverLicenseService {

	public DriverLicenseDaoImpl getDriverLicenseDaoImpl() {
		return driverLicenseDaoImpl;
	}

	public void setDriverLicenseDaoImpl(DriverLicenseDaoImpl driverLicenseDaoImpl) {
		this.driverLicenseDaoImpl = driverLicenseDaoImpl;
	}

	private DriverLicenseDaoImpl driverLicenseDaoImpl;

	public Page<Object[]> listDriverLicense(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return driverLicenseDaoImpl.listDriverLicense(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TDriverLicense transientInstance) {
		driverLicenseDaoImpl.save(transientInstance);
	}
	
	public void update(TDriverLicense transientInstance) {
		driverLicenseDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		driverLicenseDaoImpl.deleteAll(ids);
	}
}
