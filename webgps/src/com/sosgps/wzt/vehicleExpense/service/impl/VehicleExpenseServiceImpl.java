package com.sosgps.wzt.vehicleExpense.service.impl;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TVehicleExpense;
import com.sosgps.wzt.vehicleExpense.dao.hibernate.VehicleExpenseDaoImpl;
import com.sosgps.wzt.vehicleExpense.service.VehicleExpenseService;

public class VehicleExpenseServiceImpl implements VehicleExpenseService {

	public VehicleExpenseDaoImpl getVehicleExpenseDaoImpl() {
		return vehicleExpenseDaoImpl;
	}

	public void setVehicleExpenseDaoImpl(VehicleExpenseDaoImpl vehicleExpenseDaoImpl) {
		this.vehicleExpenseDaoImpl = vehicleExpenseDaoImpl;
	}

	private VehicleExpenseDaoImpl vehicleExpenseDaoImpl;

	public Page<Object[]> listVehicleExpense(String entCode, Long userId, int pageNo,
			int pageSize, Date startDate, Date endDate, String searchValue) {
		return vehicleExpenseDaoImpl.listVehicleExpense(entCode, userId, pageNo, pageSize,startDate,endDate,searchValue);
	}
	
	public void save(TVehicleExpense transientInstance) {
		vehicleExpenseDaoImpl.save(transientInstance);
	}
	
	public void update(TVehicleExpense transientInstance) {
		vehicleExpenseDaoImpl.update(transientInstance);
	}
	
	public void deleteAll(final String ids) {
		vehicleExpenseDaoImpl.deleteAll(ids);
	}
	
	public Page<Object[]> listVehicleMsg(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue, String carTypeInfoId){
		return vehicleExpenseDaoImpl.listVehicleMsg(deviceIds, entCode, userId, startint, limitint, startDate, endDate, searchValue, carTypeInfoId);
		
	}
}
