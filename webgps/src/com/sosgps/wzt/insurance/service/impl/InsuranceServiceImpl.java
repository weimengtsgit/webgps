package com.sosgps.wzt.insurance.service.impl;

import java.util.Date;
import com.sosgps.wzt.insurance.dao.hibernate.InsuranceDaoImpl;
import com.sosgps.wzt.insurance.service.InsuranceService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TInsurance;

public class InsuranceServiceImpl implements InsuranceService {

	public InsuranceDaoImpl getInsuranceDaoImpl() {
		return insuranceDaoImpl;
	}

	public void setInsuranceDaoImpl(InsuranceDaoImpl insuranceDaoImpl) {
		this.insuranceDaoImpl = insuranceDaoImpl;
	}

	private InsuranceDaoImpl insuranceDaoImpl;

	public Page<Object[]> listInsurance(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return insuranceDaoImpl.listInsurance(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TInsurance transientInstance) {
		insuranceDaoImpl.save(transientInstance);
	}
	
	public void update(TInsurance transientInstance) {
		insuranceDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		insuranceDaoImpl.deleteAll(ids);
	}
}
