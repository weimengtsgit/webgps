package com.sosgps.wzt.carTypeInfo.service.impl;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.CarTypeInfo;
import com.sosgps.wzt.carTypeInfo.dao.hibernate.CarTypeInfoDaoImpl;
import com.sosgps.wzt.carTypeInfo.service.CarTypeInfoService;

public class CarTypeInfoServiceImpl implements CarTypeInfoService {


	private CarTypeInfoDaoImpl carTypeInfoDaoImpl;

	public CarTypeInfoDaoImpl getCarTypeInfoDaoImpl() {
		return carTypeInfoDaoImpl;
	}

	public void setCarTypeInfoDaoImpl(CarTypeInfoDaoImpl carTypeInfoDaoImpl) {
		this.carTypeInfoDaoImpl = carTypeInfoDaoImpl;
	}

	public Page<Object[]> listCarTypeInfo(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return carTypeInfoDaoImpl.listCarTypeInfo(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(CarTypeInfo transientInstance) {
		carTypeInfoDaoImpl.save(transientInstance);
	}
	
	public void update(CarTypeInfo transientInstance) {
		carTypeInfoDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		carTypeInfoDaoImpl.deleteAll(ids);
	}
}
