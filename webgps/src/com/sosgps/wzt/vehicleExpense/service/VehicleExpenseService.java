package com.sosgps.wzt.vehicleExpense.service;

import java.util.Date;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TVehicleExpense;

public interface VehicleExpenseService {

	public Page<Object[]> listVehicleExpense(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue);
	public void save(TVehicleExpense transientInstance);
	public void update(TVehicleExpense transientInstance);
	public void deleteAll(final String ids);
	public Page<Object[]> listVehicleMsg(String deviceIds,
			String entCode, Long userId, int startint, int limitint,
			String startDate, String endDate, String searchValue, String carTypeInfoId);
	
}
