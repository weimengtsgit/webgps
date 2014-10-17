package com.sosgps.wzt.toll.service.impl;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TToll;
import com.sosgps.wzt.toll.dao.hibernate.TollDaoImpl;
import com.sosgps.wzt.toll.service.TollService;

public class TollServiceImpl implements TollService {

	public TollDaoImpl getTollDaoImpl() {
		return tollDaoImpl;
	}

	public void setTollDaoImpl(TollDaoImpl tollDaoImpl) {
		this.tollDaoImpl = tollDaoImpl;
	}

	private TollDaoImpl tollDaoImpl;

	public Page<Object[]> listToll(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return tollDaoImpl.listToll(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TToll transientInstance) {
		tollDaoImpl.save(transientInstance);
	}
	
	public void update(TToll transientInstance) {
		tollDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		tollDaoImpl.deleteAll(ids);
	}
}
