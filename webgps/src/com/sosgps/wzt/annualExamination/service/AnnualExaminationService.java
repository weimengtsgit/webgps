package com.sosgps.wzt.annualExamination.service;

import java.util.Date;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAnnualExamination;
import com.sosgps.wzt.orm.TInsurance;

public interface AnnualExaminationService {

	public Page<Object[]> listAnnualExamination(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TAnnualExamination transientInstance);
	public void update(TAnnualExamination transientInstance);
	public void deleteAll(final String ids);
	
}
