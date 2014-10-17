package com.sosgps.wzt.annualExamination.dao;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;

public interface AnnualExaminationDao {
	public Page<Object[]> listAnnualExamination(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void deleteAll(final String ids);
	
}
