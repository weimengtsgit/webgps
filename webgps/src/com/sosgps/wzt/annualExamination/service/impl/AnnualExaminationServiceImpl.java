package com.sosgps.wzt.annualExamination.service.impl;

import java.util.Date;
import com.sosgps.wzt.annualExamination.dao.hibernate.AnnualExaminationDaoImpl;
import com.sosgps.wzt.annualExamination.service.AnnualExaminationService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAnnualExamination;

public class AnnualExaminationServiceImpl implements AnnualExaminationService {

	public AnnualExaminationDaoImpl getAnnualExaminationDaoImpl() {
		return annualExaminationDaoImpl;
	}

	public void setAnnualExaminationDaoImpl(
			AnnualExaminationDaoImpl annualExaminationDaoImpl) {
		this.annualExaminationDaoImpl = annualExaminationDaoImpl;
	}

	private AnnualExaminationDaoImpl annualExaminationDaoImpl;

	public Page<Object[]> listAnnualExamination(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return annualExaminationDaoImpl.listAnnualExamination(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TAnnualExamination transientInstance) {
		annualExaminationDaoImpl.save(transientInstance);
	}
	
	public void update(TAnnualExamination transientInstance) {
		annualExaminationDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		annualExaminationDaoImpl.deleteAll(ids);
	}
}
