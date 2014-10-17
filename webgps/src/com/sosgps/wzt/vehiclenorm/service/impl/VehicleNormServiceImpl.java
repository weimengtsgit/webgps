package com.sosgps.wzt.vehiclenorm.service.impl;

import java.util.Date;

import com.sosgps.wzt.dispatchmoney.dao.hibernate.DispatchMoneyDaoImpl;
import com.sosgps.wzt.dispatchmoney.service.DispatchMoneyService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDispatchMoney;
import com.sosgps.wzt.orm.TVehicleNorm;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.vehiclenorm.dao.hibernate.VehicleNormDaoImpl;
import com.sosgps.wzt.vehiclenorm.service.VehicleNormService;
import com.sosgps.wzt.vehiclesMaintenance.dao.hibernate.VehiclesMaintenanceDaoImpl;
import com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService;

public class VehicleNormServiceImpl implements VehicleNormService {

	public Page<Object[]> listVehicleNorm(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue) {
		// TODO Auto-generated method stub
		return vehicleNormDaoImpl.listvehicleNormDaoImpl(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}

	public void save(TVehicleNorm transientInstance) {
		// TODO Auto-generated method stub
		vehicleNormDaoImpl.save(transientInstance);
	}

	public void update(TDispatchMoney transientInstance) {
		// TODO Auto-generated method stub
		
	}

	public void deleteAll(String ids) {
		// TODO Auto-generated method stub
		vehicleNormDaoImpl.deleteAll(ids);
		
	}
	private  VehicleNormDaoImpl   vehicleNormDaoImpl;



	public void update(TVehicleNorm transientInstance) {
		// TODO Auto-generated method stub
		vehicleNormDaoImpl.update(transientInstance);
	}

	public void setVehicleNormDaoImpl(VehicleNormDaoImpl vehicleNormDaoImpl) {
		this.vehicleNormDaoImpl = vehicleNormDaoImpl;
	}

	public VehicleNormDaoImpl getVehicleNormDaoImpl() {
		return vehicleNormDaoImpl;
	}
}
