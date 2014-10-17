package com.sosgps.wzt.dispatchmoney.service.impl;

import java.util.Date;

import com.sosgps.wzt.dispatchmoney.dao.hibernate.DispatchMoneyDaoImpl;
import com.sosgps.wzt.dispatchmoney.service.DispatchMoneyService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDispatchMoney;
import com.sosgps.wzt.orm.TVehiclesMaintenance;
import com.sosgps.wzt.vehiclesMaintenance.dao.hibernate.VehiclesMaintenanceDaoImpl;
import com.sosgps.wzt.vehiclesMaintenance.service.VehiclesMaintenanceService;

public class DispatchMoneyServiceImpl implements DispatchMoneyService {

	public Page<Object[]> listdispatchCondition(String entCode, Long userId,
			int pageNo, int pageSize, Date startDate, Date endDate,
			String searchValue) {
		// TODO Auto-generated method stub
		return dispatchMoneyDaoImpl.listdispatchCondition(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}

	public void save(TDispatchMoney transientInstance) {
		// TODO Auto-generated method stub
		dispatchMoneyDaoImpl.save(transientInstance);
	}

	public void update(TDispatchMoney transientInstance) {
		dispatchMoneyDaoImpl.update(transientInstance);
		
	}

	public void deleteAll(String ids) {
		// TODO Auto-generated method stub
		dispatchMoneyDaoImpl.deleteAll(ids);
	}
	private  DispatchMoneyDaoImpl   dispatchMoneyDaoImpl;
//	public VehiclesMaintenanceDaoImpl getVehiclesMaintenanceDaoImpl() {
//		return vehiclesMaintenanceDaoImpl;
//	}
//
//	public void setVehiclesMaintenanceDaoImpl(
//			VehiclesMaintenanceDaoImpl vehiclesMaintenanceDaoImpl) {
//		this.vehiclesMaintenanceDaoImpl = vehiclesMaintenanceDaoImpl;
//	}
//
//	private VehiclesMaintenanceDaoImpl vehiclesMaintenanceDaoImpl;
//
//	
//	public void save(TVehiclesMaintenance transientInstance) {
//		vehiclesMaintenanceDaoImpl.save(transientInstance);
//	}
//	
//	public void update(TVehiclesMaintenance transientInstance) {
//		vehiclesMaintenanceDaoImpl.update(transientInstance);
//	}
//	
//	public void deleteAll(final String ids) {
//		vehiclesMaintenanceDaoImpl.deleteAll(ids);
//	}
	public DispatchMoneyDaoImpl getDispatchMoneyDaoImpl() {
		return dispatchMoneyDaoImpl;
	}

	public void setDispatchMoneyDaoImpl(DispatchMoneyDaoImpl dispatchMoneyDaoImpl) {
		this.dispatchMoneyDaoImpl = dispatchMoneyDaoImpl;
	}
}
