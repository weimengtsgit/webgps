package com.sosgps.wzt.oiling.service.impl;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.oiling.dao.hibernate.OilingDaoImpl;
import com.sosgps.wzt.oiling.service.OilingService;
import com.sosgps.wzt.orm.TOiling;

public class OilingServiceImpl implements OilingService {

	public OilingDaoImpl getOilingDaoImpl() {
		return oilingDaoImpl;
	}

	public void setOilingDaoImpl(OilingDaoImpl oilingDaoImpl) {
		this.oilingDaoImpl = oilingDaoImpl;
	}

	private OilingDaoImpl oilingDaoImpl;

	public Page<Object[]> listOiling(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return oilingDaoImpl.listOiling(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TOiling transientInstance) {
		oilingDaoImpl.save(transientInstance);
	}
	
	public void update(TOiling transientInstance) {
		oilingDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		oilingDaoImpl.deleteAll(ids);
	}
}
